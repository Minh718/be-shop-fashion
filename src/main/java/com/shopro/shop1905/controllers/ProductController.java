package com.shopro.shop1905.controllers;

import java.util.List;
import java.util.Set;

import javax.print.attribute.standard.Media;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shopro.shop1905.dtos.dtosReq.ProductAddDTO;
import com.shopro.shop1905.dtos.dtosReq.IdProductSizeIdColorDTO;
import com.shopro.shop1905.dtos.dtosRes.ApiMetaRes;
import com.shopro.shop1905.dtos.dtosRes.ApiRes;
import com.shopro.shop1905.dtos.dtosRes.MetadataDTO;
import com.shopro.shop1905.dtos.dtosRes.ProductDTO;
import com.shopro.shop1905.dtos.dtosRes.ProductDetailAdmin;
import com.shopro.shop1905.dtos.dtosRes.ProductDetailDTO;
import com.shopro.shop1905.dtos.dtosRes.ProductSizeColorDTO;
import com.shopro.shop1905.dtos.dtosRes.ProductsHomePage;
import com.shopro.shop1905.dtos.dtosRes.projections.ProductTableProjection;
import com.shopro.shop1905.repositories.ProductRepository;
import com.shopro.shop1905.services.ProductService;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * ProductController
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("api/product")
@Slf4j
public class ProductController {

        private final ProductService productService;
        private final ProductRepository productRepository;

        @Caching(evict = {
                        @CacheEvict(value = "productsBySubCategory", allEntries = true),
                        @CacheEvict(value = "ListProductsForHomePage", allEntries = true)
        })
        @PostMapping("/updateQuantity")
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<ApiRes<Void>> updateQuantityForProduct(
                        @RequestBody IdProductSizeIdColorDTO updateQuantityDTO) {
                productService.updateQuantityForProduct(updateQuantityDTO);
                return ResponseEntity.ok().body(ApiRes.<Void>builder()
                                .code(1000)
                                .message("update sản phẩm thành công")
                                .build());
        }

        @Caching(evict = {
                        @CacheEvict(value = "productsBySubCategory", allEntries = true),
                        @CacheEvict(value = "ListProductsForHomePage", allEntries = true)
        })
        @PostMapping("/addQuantity")
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<ApiRes<Void>> adQuantityForProduct(
                        @RequestBody IdProductSizeIdColorDTO updateQuantityDTO) {
                productService.addQuantityForProduct(updateQuantityDTO);
                return ResponseEntity.ok().body(ApiRes.<Void>builder()
                                .code(1000)
                                .message("update quantity for product successfully")
                                .build());
        }

        @Caching(evict = {
                        @CacheEvict(value = "productsBySubCategory", allEntries = true),
                        @CacheEvict(value = "ListProductsForHomePage", allEntries = true)
        })
        @PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
        @PreAuthorize("hasRole('ADMIN')")
        public ApiRes<ProductDTO> addProduct(@ModelAttribute ProductAddDTO productDTO) {
                log.info("admin add new product" + productDTO.name());
                return ApiRes.<ProductDTO>builder()
                                .code(1000)
                                .result(productService.addProduct(productDTO))
                                .message("Add product successfully")
                                .build();
        }

        @Caching(evict = {
                        @CacheEvict(value = "productsBySubCategory", allEntries = true),
                        @CacheEvict(value = "ListProductsForHomePage", allEntries = true)
        })
        @DeleteMapping("/{idProduct}")
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<ApiRes<Void>> deleteProduct(
                        @NotNull(message = "idProduct is required") @PathVariable Long idProduct) {
                log.info("admin delete product" + idProduct);
                productService.deleteProduct(idProduct);
                return ResponseEntity.ok().body(ApiRes.<Void>builder()
                                .code(1000)
                                .message("delete sản phẩm thành công")
                                .build());
        }

        @Caching(evict = {
                        @CacheEvict(value = "productsBySubCategory", allEntries = true),
                        @CacheEvict(value = "ListProductsForHomePage", allEntries = true)
        })
        @GetMapping("/publish/{id}")
        @PreAuthorize("hasRole('ROLE_ADMIN')")
        public ApiRes<Void> publishProduct(@PathVariable long id) {
                return ApiRes.<Void>builder().code(1000)
                                .message("public product success")
                                .result(productService.publishProduct(id)).build();
        }

        @GetMapping("/draft/{id}")
        @PreAuthorize("hasRole('ROLE_ADMIN')")
        public ApiRes<Void> draftProduct(@PathVariable long id) {
                return ApiRes.<Void>builder().code(1000)
                                .message("draft product success")
                                .result(productService.draftProduct(id)).build();
        }

        @Cacheable(value = "ListProductsForHomePage")
        @GetMapping("/public/homepage")
        public ApiRes<List<ProductsHomePage>> getListProductsForHomePage() {
                return ApiRes.<List<ProductsHomePage>>builder().code(1000)
                                .message("get product detail success")
                                .result(productService.getListProductsForHomePage()).build();
        }

        @GetMapping("/{id}")
        public ApiRes<ProductDetailDTO> getProductDetail(@PathVariable long id) {
                return ApiRes.<ProductDetailDTO>builder().code(1000)
                                .message("get product detail success")
                                .result(productService.getProductDetail(id)).build();
        }

        @GetMapping("/admin/{id}")
        public ApiRes<ProductDetailAdmin> getProductDetailAdmin(@PathVariable long id) {
                return ApiRes.<ProductDetailAdmin>builder().code(1000)
                                .message("get product detail success")
                                .result(productService.getProductDetailAdmin(id)).build();
        }

        @GetMapping("/public")
        public ApiMetaRes<List<ProductDTO>> getPublicProducts(
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "10") int size,
                        @RequestParam(defaultValue = "newest") String sortBy,
                        @RequestParam(defaultValue = "asc") String order) {
                Page<ProductDTO> productPage = productService.getPublicProducts(page, size, sortBy, order);
                MetadataDTO metadata = new MetadataDTO(
                                productPage.getTotalElements(),
                                productPage.getTotalPages(),
                                productPage.getNumber(),
                                productPage.getSize());
                return ApiMetaRes.<List<ProductDTO>>builder().code(1000).message("lấy danh sách thành công")
                                .result(productPage.getContent()).metadata(metadata).build();
        }

        @GetMapping("/table/admin")
        @PreAuthorize("hasRole('ROLE_ADMIN')")
        public ApiMetaRes<List<ProductTableProjection>> getProductsForAdminTable(
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "10") int size,
                        @RequestParam(defaultValue = "createdDate") String sortBy,
                        @RequestParam(defaultValue = "desc") String order) {
                Page<ProductTableProjection> productPage = productService.getProductsForAdminTable(page, size, sortBy,
                                order);
                MetadataDTO metadata = new MetadataDTO(
                                productPage.getTotalElements(),
                                productPage.getTotalPages(),
                                productPage.getNumber(),
                                productPage.getSize());
                return ApiMetaRes.<List<ProductTableProjection>>builder().code(1000).message("lấy danh sách thành công")
                                .result(productPage.getContent()).metadata(metadata).build();
        }

        @GetMapping("/table/admin/category/{id}")
        @PreAuthorize("hasRole('ROLE_ADMIN')")
        public ApiMetaRes<List<ProductTableProjection>> getProductsForAdminTableByCategory(
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "10") int size,
                        @RequestParam(defaultValue = "createdDate") String sortBy,
                        @RequestParam(defaultValue = "desc") String order,
                        @PathVariable Long id) {
                Page<ProductTableProjection> productPage = productService.getProductsForAdminTableByCategory(page, size,
                                sortBy,
                                order, id);
                MetadataDTO metadata = new MetadataDTO(
                                productPage.getTotalElements(),
                                productPage.getTotalPages(),
                                productPage.getNumber(),
                                productPage.getSize());
                return ApiMetaRes.<List<ProductTableProjection>>builder().code(1000).message("lấy danh sách thành công")
                                .result(productPage.getContent()).metadata(metadata).build();
        }

        @GetMapping("/table/admin/subcategory/{id}")
        @PreAuthorize("hasRole('ROLE_ADMIN')")
        public ApiMetaRes<List<ProductTableProjection>> getProductsForAdminTableBySubCategory(
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "10") int size,
                        @RequestParam(defaultValue = "createdDate") String sortBy,
                        @RequestParam(defaultValue = "desc") String order,
                        @PathVariable Long id) {
                Page<ProductTableProjection> productPage = productService.getProductsForAdminTableBySubCategory(page,
                                size, sortBy,
                                order, id);
                MetadataDTO metadata = new MetadataDTO(
                                productPage.getTotalElements(),
                                productPage.getTotalPages(),
                                productPage.getNumber(),
                                productPage.getSize());
                return ApiMetaRes.<List<ProductTableProjection>>builder().code(1000).message("lấy danh sách thành công")
                                .result(productPage.getContent()).metadata(metadata).build();
        }

        @Cacheable(value = "publicProductsBySubCategory", key = "#page + '-' + #size + '-' + #thump + '-' + #sortBy + '-' + #order", condition = "#page == 0")
        @GetMapping("/public/subCategory")
        public ApiMetaRes<List<ProductDTO>> getPublicProductsBySubCategory(
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "10") int size,
                        @RequestParam String thump,
                        @RequestParam(defaultValue = "newest") String sortBy,
                        @RequestParam(defaultValue = "asc") String order) {

                Page<ProductDTO> productPage = productService.getPublicProductsBySubCategory(page, size, thump, sortBy,
                                order);
                MetadataDTO metadata = new MetadataDTO(
                                productPage.getTotalElements(),
                                productPage.getTotalPages(),
                                productPage.getNumber(),
                                productPage.getSize());
                return ApiMetaRes.<List<ProductDTO>>builder()
                                .code(1000)
                                .message("lấy danh sách thành công")
                                .result(productPage.getContent())
                                .metadata(metadata)
                                .build();
        }

        @GetMapping("/draft")
        @PreAuthorize("hasRole('ROLE_ADMIN')")
        public ApiMetaRes<List<ProductDTO>> getDraftProducts(
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "10") int size) {
                Page<ProductDTO> productPage = productService.getDraftProducts(page, size);
                MetadataDTO metadata = new MetadataDTO(
                                productPage.getTotalElements(),
                                productPage.getTotalPages(),
                                productPage.getNumber(),
                                productPage.getSize());

                return ApiMetaRes.<List<ProductDTO>>builder().code(1000).message("lấy danh sách thành công")
                                .result(productPage.getContent()).metadata(metadata).build();

        }
}