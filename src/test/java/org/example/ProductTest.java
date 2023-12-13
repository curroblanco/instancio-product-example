package org.example;

import org.example.mother.ProductMother;
import org.example.product.Product;
import org.instancio.Instancio;
import org.instancio.Model;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Select.field;

class ProductTest {

    @Test
    void buildRandomValuedProduct() {
        final Model<Product> productModel = new ProductMother().buildDefaultInstance();
        final Product product = Instancio.of(productModel)
                .create();

        assertThat(product).isNotNull();
        assertThat(product.getId()).isNotNull();
        assertThat(product.getPartNumber()).isNotNull();
        assertThat(product.getFirstAvailabilityDate()).isNotNull();
        assertThat(product.getUnPublicationDate()).isNotNull();

        assertThat(product.isActive()).isTrue();
        assertThat(product.isValidPartNumber()).isTrue();
    }

    @Test
    void buildInvalidProduct() {
        final Model<Product> productModel = new ProductMother().buildDefaultInstance();
        final Product product = Instancio.of(productModel)
                .set(field(Product::getPartNumber), "FAKE-VALUE")
                .set(field(Product::getFirstAvailabilityDate), LocalDate.now().plusYears(10L))
                .create();

        assertThat(product).isNotNull();
        assertThat(product.getId()).isNotNull();
        assertThat(product.getPartNumber()).isNotNull();
        assertThat(product.getFirstAvailabilityDate()).isNotNull();
        assertThat(product.getUnPublicationDate()).isNotNull();

        assertThat(product.isActive()).isFalse();
        assertThat(product.isValidPartNumber()).isFalse();
    }
}
