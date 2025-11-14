package com.deliverytech.delivery.dto.shared;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Resposta padronizada para listagens paginadas
 * @param <T> Tipo dos itens na página
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Resposta padronizada para listagens paginadas")
public class PagedResponse<T> {

  @Schema(description = "Timestamp da resposta", example = "2025-11-13T10:30:00")
  private LocalDateTime timestamp;

  @Schema(description = "Código HTTP da resposta", example = "200")
  private int statusCode;

  @Schema(description = "Mensagem de sucesso", example = "Listagem realizada com sucesso")
  private String message;

  @Schema(description = "Sucesso da operação", example = "true")
  private boolean success;

  @Schema(description = "Dados da página atual")
  private List<T> data;

  @Schema(description = "Metadados da paginação")
  private PaginationMetadata pagination;

  @Schema(description = "Links de navegação")
  private NavigationLinks links;

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class PaginationMetadata {
    @Schema(description = "Número da página atual (iniciando em 0)", example = "0")
    private int currentPage;

    @Schema(description = "Tamanho da página", example = "20")
    private int pageSize;

    @Schema(description = "Total de itens", example = "150")
    private long totalItems;

    @Schema(description = "Total de páginas", example = "8")
    private int totalPages;

    @Schema(description = "Se há próxima página", example = "true")
    private boolean hasNext;

    @Schema(description = "Se há página anterior", example = "false")
    private boolean hasPrevious;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class NavigationLinks {
    @Schema(description = "Link para a primeira página")
    private String first;

    @Schema(description = "Link para a última página")
    private String last;

    @Schema(description = "Link para a próxima página")
    private String next;

    @Schema(description = "Link para a página anterior")
    private String previous;

    @Schema(description = "Link para a página atual")
    private String current;
  }

  public static <T> PagedResponse<T> of(
      List<T> data,
      int currentPage,
      int pageSize,
      long totalItems,
      String baseUrl) {
    int totalPages = (int) Math.ceil((double) totalItems / pageSize);
    boolean hasNext = currentPage < (totalPages - 1);
    boolean hasPrevious = currentPage > 0;

    PaginationMetadata pagination =
        PaginationMetadata.builder()
            .currentPage(currentPage)
            .pageSize(pageSize)
            .totalItems(totalItems)
            .totalPages(totalPages)
            .hasNext(hasNext)
            .hasPrevious(hasPrevious)
            .build();

    NavigationLinks links =
        NavigationLinks.builder()
            .first(baseUrl + "?page=0&size=" + pageSize)
            .last(baseUrl + "?page=" + (totalPages - 1) + "&size=" + pageSize)
            .next(hasNext ? baseUrl + "?page=" + (currentPage + 1) + "&size=" + pageSize : null)
            .previous(hasPrevious ? baseUrl + "?page=" + (currentPage - 1) + "&size=" + pageSize : null)
            .current(baseUrl + "?page=" + currentPage + "&size=" + pageSize)
            .build();

    return PagedResponse.<T>builder()
        .timestamp(LocalDateTime.now())
        .statusCode(200)
        .message("Listagem realizada com sucesso")
        .success(true)
        .data(data)
        .pagination(pagination)
        .links(links)
        .build();
  }
}
