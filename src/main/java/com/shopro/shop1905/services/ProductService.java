package com.shopro.shop1905.services;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.shopro.shop1905.dtos.dtosReq.IdProductSizeIdColorDTO;
import com.shopro.shop1905.dtos.dtosReq.ProductAddDTO;
import com.shopro.shop1905.dtos.dtosRes.ColorQuantityRes;
import com.shopro.shop1905.dtos.dtosRes.ProductDTO;
import com.shopro.shop1905.dtos.dtosRes.ProductDetailAdmin;
import com.shopro.shop1905.dtos.dtosRes.ProductDetailDTO;
import com.shopro.shop1905.dtos.dtosRes.ProductSizeQuantity;
import com.shopro.shop1905.dtos.dtosRes.ProductsHomePage;
import com.shopro.shop1905.dtos.dtosRes.projections.ProductTableProjection;
import com.shopro.shop1905.entities.Brand;
import com.shopro.shop1905.entities.Category;
import com.shopro.shop1905.entities.Color;
import com.shopro.shop1905.entities.DetailProduct;
import com.shopro.shop1905.entities.Product;
import com.shopro.shop1905.entities.ProductDocument;
import com.shopro.shop1905.entities.ProductSize;
import com.shopro.shop1905.entities.ProductSizeColor;
import com.shopro.shop1905.entities.Size;
import com.shopro.shop1905.entities.SubCategory;
import com.shopro.shop1905.exceptions.CustomException;
import com.shopro.shop1905.exceptions.ErrorCode;
import com.shopro.shop1905.mappers.ProductMapper;
import com.shopro.shop1905.repositories.BrandRepository;
import com.shopro.shop1905.repositories.CategoryRepository;
import com.shopro.shop1905.repositories.ColorRepository;
import com.shopro.shop1905.repositories.ProductElasticsearchRepository;
import com.shopro.shop1905.repositories.ProductRepository;
import com.shopro.shop1905.repositories.ProductSizeColorRepository;
import com.shopro.shop1905.repositories.ProductSizeRepository;
import com.shopro.shop1905.repositories.SubCategoryRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductSizeRepository productSizeRepository;
    private final ProductElasticsearchRepository productElasticsearchRepository;
    private final SubCategoryRepository subCategoryRepository;
    private final CategoryRepository categoryRepository;
    private final RedisService redisService;
    private final ColorRepository colorRepository;
    private final SizeService sizeService;
    private final BrandRepository brandRepository;
    private final ProductSizeColorRepository productSizeColorRepository;
    @NonFinal
    private final String uploadDir = "uploads/";

    public Page<ProductDTO> searchByName(String name, int size, int page, String sortBy,
            String orderBy) {
        Pageable pageable;
        if (orderBy.equals("asc")) {
            pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, sortBy));
        } else {
            pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, sortBy));
        }

        Page<ProductDocument> products = productElasticsearchRepository.findByNameAndStatus(name, true,
                pageable);
        Page<ProductDTO> productDTOs = products.map(ProductMapper.INSTANCE::toProductDTO);
        return productDTOs;
    }

    public void syncProductsFromMySQLToElasticsearch() {
        // Fetch all products from MySQL
        List<Product> productEntities = productRepository.findAll();

        // Convert ProductEntity to ProductDocument
        for (Product productEntity : productEntities) {
            ProductDocument productDocument = ProductMapper.INSTANCE.toProductDocument(productEntity);
            // Save to Elasticsearch
            productElasticsearchRepository.save(productDocument);
        }
    }

    public ProductDTO addProduct(ProductAddDTO productAddDTO) {
        // Find subcategory by id with category
        SubCategory subCategory = subCategoryRepository.findByIdWithCategory(productAddDTO.subCate_id())
                .orElseThrow(() -> new CustomException(ErrorCode.SUBCATEGORY_NOT_EXISTED));
        Category category = subCategory.getCategory();

        Brand brand = brandRepository.findById(productAddDTO.brand_id())
                .orElseThrow(() -> new CustomException(ErrorCode.BRAND_NOT_EXISTED));

        List<ProductSize> productSizes = new LinkedList<>();
        for (Size size : category.getSizes()) {
            ProductSize productSize = new ProductSize();
            productSize.setSize(size);
            productSizes.add(productSize);
        }
        String nameImage = saveImage(productAddDTO.file());

        List<String> images = new LinkedList<>();
        for (MultipartFile file : productAddDTO.files()) {
            images.add(saveImage(file));
        }

        DetailProduct detailProduct = DetailProduct.builder()
                .description(productAddDTO.description())
                .model(productAddDTO.model())
                .material(productAddDTO.material())
                .origin(productAddDTO.origin())
                .warranty(productAddDTO.warranty())
                .madeIn(productAddDTO.madeIn())
                .images(images)
                .build();
        Product product = Product.builder()
                .name(productAddDTO.name())
                .price(productAddDTO.price())
                .percent(productAddDTO.percent())
                .categoryId(category.getId())
                .status(productAddDTO.status())
                .subCategory(subCategory)
                .brand(brand)
                .image(nameImage)
                .detailProduct(detailProduct)
                .productSizes(productSizes)
                .build();
        // Save product again to update the list of product sizes
        productRepository.save(product);
        productElasticsearchRepository.save(ProductMapper.INSTANCE.toProductDocument(product));
        return ProductMapper.INSTANCE.toProductDTO(product);
    }

    private void validateFile(MultipartFile file) {
        String fileType = file.getContentType();

        if (!fileType.equals("image/png") && !fileType.equals("image/jpeg")) {
            throw new RuntimeException("Invalid file type. Only PNG and JPEG are allowed.");
        }
    }

    private String saveImage(MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new RuntimeException("Image file is required");
            }
            validateFile(file);
            // Create unique image name
            String imageName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path path = Paths.get(uploadDir + imageName);

            // Create directories if they do not exist
            Files.createDirectories(path.getParent());

            // Save the file to the directory
            Files.write(path, file.getBytes());

            return imageName;
        } catch (Exception e) {
            throw new RuntimeException("Failed to store image file", e);
        }
    }

    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_EXISTED));
        productRepository.delete(product);
        productElasticsearchRepository.deleteById(product.getId());
    }

    public Page<ProductDTO> getProducts(int page, int size) {
        // Pageable pageable = PageRequest.of(page, size);
        // Page<Product> products =
        // productRepository.findAllByOrderByCreatedDateDesc(pageable);
        // Page<ProductDTO> productDTOs =
        // products.map(ProductMapper.INSTANCE::toProductDTO);
        // return productDTOs;
        return null;
    }

    public int processing(ProductSizeQuantity productSize) {
        int count = (int) (Math.random() * 4);
        if (count % 4 == 0) {
            redisService.setKey("productSize:" + productSize.getId(), 0);
            count++;
            return 0;
        }
        int totalQuantity = 0;
        for (ColorQuantityRes productSizeColor : productSize.getProductSizeColors()) {
            int quantity = (int) (Math.random() * 80);
            totalQuantity += quantity;
            redisService.setKey("productSizeColor:" + productSizeColor.getId(), quantity);
        }
        return totalQuantity;
    }

    public ProductDetailDTO getProductDetail(Long id) {
        // Product product = productRepository.findById(id)
        // .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_EXISTED));
        Product product = productRepository.findByIdAndFetchProductSizesAndFetchDetailProduct(id)
                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_EXISTED));

        // will be removed
        if (product.getDetailProduct().getImages().size() == 0) {
            List<String> images = new LinkedList<>();
            images.add("https://picsum.photos/201/300");
            images.add("https://picsum.photos/202/300");
            images.add("https://picsum.photos/203/300");
            product.getDetailProduct().setImages(images);
            productRepository.save(product);
        }
        ProductDetailDTO productDetailDTO = ProductMapper.INSTANCE.toProductDetailDTO(product);
        productDetailDTO.getProductSizes().forEach(productSize -> {
            var quantityPZ = redisService.getKey("productSize:" + productSize.getId());
            if (quantityPZ == null) {
                // will be removed
                quantityPZ = processing(productSize);
                redisService.setKey("productSize:" + productSize.getId(), quantityPZ);
            }
            productSize.setQuantity((int) quantityPZ);
            if ((int) quantityPZ != 0) {
                productSize.getProductSizeColors().forEach(productSizeColor -> {

                    var quantityPSC = redisService.getKey("productSizeColor:" +
                            productSizeColor.getId());
                    if (quantityPSC == null) {
                        quantityPSC = 0;
                    }
                    productSizeColor.setQuantity((int) quantityPSC);
                });
            }
        });
        return productDetailDTO;
    }

    public ProductDetailAdmin getProductDetailAdmin(Long id) {
        Product product = productRepository.findByIdAndFetchDetailProduct(id)
                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_EXISTED));
        ProductDetailAdmin productDetailAdmin = ProductMapper.INSTANCE.toProductDetailAdmin(product);
        productDetailAdmin.getDetailProduct().getImages().add(product.getImage());
        productDetailAdmin.setSizes(sizeService.getAllSizeOfProductForAdmin(id));
        return productDetailAdmin;
    }

    // public void updateQuantityForProduct(UpdateQuantityDTO updateQuantityDTO) {
    // ProductSize productSize =
    // productSizeRepository.findById(updateQuantityDTO.getIdProductSize())
    // .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_SIZE_NOT_EXISTED));
    // productSize.setProductSizeColors(null);
    // productSizeRepository.save(productSize);
    // long totalQuantity = 0;
    // // Set<ProductSizeColor> productSizeColors = new HashSet<>();
    // for (ColorQuantity colorQuantity : updateQuantityDTO.getColorQuantities()) {
    // ProductSizeColor productSizeColor = new ProductSizeColor();
    // Color color = colorRepository.findById(colorQuantity.getIdColor())
    // .orElseThrow(() -> new CustomException(ErrorCode.COLOR_NOT_EXISTED));
    // productSizeColor.setColor(color);
    // productSizeColor.setProductSize(productSize);
    // productSizeColorRepository.save(productSizeColor);
    // redisService.setKey("productSizeColor:" + productSizeColor.getId(),
    // colorQuantity.getQuantity());
    // // productSizeColors.add(productSizeColor);
    // totalQuantity += colorQuantity.getQuantity();
    // }
    // redisService.setKey("productSize:" + productSize.getId(), totalQuantity);
    // }
    @Transactional
    public void addQuantityForProduct(IdProductSizeIdColorDTO productSizeColorDTO) {
        Color color = colorRepository.findById(productSizeColorDTO.getIdColor())
                .orElseThrow(() -> new CustomException(ErrorCode.COLOR_NOT_EXISTED));
        ProductSize productSize = productSizeRepository.findById(productSizeColorDTO.getIdProductSize())
                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_SIZE_NOT_EXISTED));
        productSizeColorRepository.findByProductSizeAndColor(productSize, color).ifPresent(productSizeColor -> {
            throw new CustomException(ErrorCode.PRODUCT_SIZE_COLOR_EXISTED);
        });
        ProductSizeColor productSizeColor = ProductSizeColor.builder().color(color).productSize(productSize).build();
        productSizeColorRepository.save(productSizeColor);
        redisService.setKey("productSizeColor:" + productSizeColor.getId(), productSizeColorDTO.getQuantity());
        redisService.incrementKey("productSize:" + productSize.getId(), productSizeColorDTO.getQuantity());
    }

    @Transactional
    public void updateQuantityForProduct(IdProductSizeIdColorDTO productSizeColorDTO) {
        if (productSizeColorDTO.getQuantity() < 0) {
            throw new CustomException(ErrorCode.INVALID_QUANTITY);
        }
        Color color = colorRepository.findById(productSizeColorDTO.getIdColor())
                .orElseThrow(() -> new CustomException(ErrorCode.COLOR_NOT_EXISTED));
        ProductSize productSize = productSizeRepository.findById(productSizeColorDTO.getIdProductSize())
                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_SIZE_NOT_EXISTED));
        ProductSizeColor productSizeColor = productSizeColorRepository.findByProductSizeAndColor(productSize, color)
                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_SIZE_COLOR_NOT_EXISTED));
        int oldQuantity = (int) redisService.getKey("productSizeColor:" +
                productSizeColor.getId());
        int newQuantity = productSizeColorDTO.getQuantity();
        redisService.setKey("productSizeColor:" + productSizeColor.getId(),
                newQuantity);
        redisService.incrementKey("productSize:" + productSize.getId(), newQuantity -
                oldQuantity);
    }

    public Page<ProductDTO> getPublicProducts(int page, int size, String sortBy, String orderBy) {
        Pageable pageable;
        if (sortBy.equals("newest")) {
            pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdDate"));

        } else if (orderBy.equals("asc")) {
            pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, sortBy));
        } else {
            pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, sortBy));
        }
        Page<ProductDocument> products = productElasticsearchRepository.findByStatus(true,
                pageable);
        Page<ProductDTO> productDTOs = products.map(ProductMapper.INSTANCE::toProductDTO);
        return productDTOs;
    }

    public Page<ProductTableProjection> getProductsForAdminTable(int page, int size, String sortBy, String orderBy) {
        Pageable pageable;
        if (orderBy.equals("asc")) {
            pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, sortBy));
        } else {
            System.err.println(sortBy);
            pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, sortBy));
        }
        return productRepository.findAllProductForTable(pageable);
    }

    public Page<ProductTableProjection> getProductsForAdminTableByCategory(int page, int size, String sortBy,
            String orderBy, Long idCategory) {
        Pageable pageable;
        if (orderBy.equals("asc")) {
            pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, sortBy));
        } else {
            System.err.println(sortBy);
            pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, sortBy));
        }
        return productRepository.findAllProductForTableByCategory(idCategory, pageable);
    }

    public Page<ProductTableProjection> getProductsForAdminTableBySubCategory(int page, int size, String sortBy,
            String orderBy, Long idSubCategory) {
        Pageable pageable;
        if (orderBy.equals("asc")) {
            pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, sortBy));
        } else {
            System.err.println(sortBy);
            pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, sortBy));
        }
        return productRepository.findAllProductForTableBySubCategory(idSubCategory, pageable);
    }

    public Page<ProductDTO> getPublicProductsBySubCategory(int page, int size, String thump, String sortBy,
            String orderBy) {
        Pageable pageable;
        if (orderBy.equals("asc")) {
            pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, sortBy));
        } else {
            pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, sortBy));
        }
        Page<ProductDocument> products = productElasticsearchRepository.findByStatusAndThump(true,
                thump, pageable);
        Page<ProductDTO> productDTOs = products.map(ProductMapper.INSTANCE::toProductDTO);
        return productDTOs;
    }

    public List<ProductDTO> getPublicNewestProductsByCategory(int size,
            Long idCategory) {
        Pageable pageable = PageRequest.of(0, size, Sort.by(Sort.Direction.DESC, "createdDate"));
        Page<ProductDocument> products = productElasticsearchRepository.findByStatusAndCategoryId(true,
                idCategory, pageable);
        Page<ProductDTO> productDTOs = products.map(ProductMapper.INSTANCE::toProductDTO);
        return productDTOs.getContent();
    }

    public Page<ProductDTO> getDraftProducts(int page, int size) {
        // Pageable pageable = PageRequest.of(page, size);
        // Page<Product> products =
        // productRepository.findAllByIsDraftOrderByCreatedDateDesc(true,
        // pageable);
        // Page<ProductDTO> productDTOs =
        // products.map(ProductMapper.INSTANCE::toProductDTO);
        // return productDTOs;
        return null;
    }

    public List<ProductsHomePage> getListProductsForHomePage() {
        List<Category> categories = categoryRepository.findAll();
        List<ProductsHomePage> listProductsHomePage = new LinkedList<>();
        for (Category category : categories) {
            List<ProductDTO> products = getPublicNewestProductsByCategory(10,
                    category.getId());
            listProductsHomePage.add(new ProductsHomePage(category.getName(), products));
        }
        return listProductsHomePage;
    }

    public Void publishProduct(Long id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_EXISTED));
        product.setStatus(true);
        productRepository.save(product);
        return null;
    }

    public Void draftProduct(Long id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_EXISTED));
        product.setStatus(false);
        productRepository.save(product);
        return null;
    }
}
