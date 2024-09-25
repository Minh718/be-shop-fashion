// package com.shopro.shop1905.util;

// import org.springframework.data.domain.Page;

// import com.shopro.shop1905.dtos.dtosRes.MetadataDTO;
// import com.shopro.shop1905.dtos.dtosRes.ProductDTO;

// import java.util.List;

// public class PaginationUtil {

// public static MetadataDTO createMetadata(Page<?> page, String baseUrl, String
// thump, String sortBy,
// String order) {
// String nextPageUrl = page.hasNext() ? buildPageUrl(baseUrl, page.getNumber()
// + 1, thump, sortBy, order) : null;
// String prevPageUrl = page.hasPrevious() ? buildPageUrl(baseUrl,
// page.getNumber() - 1, thump, sortBy, order)
// : null;
// String firstPageUrl = buildPageUrl(baseUrl, 0, thump, sortBy, order);
// String lastPageUrl = buildPageUrl(baseUrl, page.getTotalPages() - 1, thump,
// sortBy, order);

// return new MetadataDTO(
// page.getTotalElements(),
// page.getTotalPages(),
// page.getNumber(),
// page.getSize(),
// nextPageUrl,
// prevPageUrl,
// lastPageUrl,
// firstPageUrl);
// }

// public static String buildPageUrl(String baseUrl, int page, String thump,
// String sortBy, String order) {
// return baseUrl + "page=" + page + "&thump=" + thump + "&sortBy=" + sortBy +
// "&order=" + order;
// }
// }
