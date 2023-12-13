package org.example;

import org.example.bundle.Bundle;
import org.example.bundle.BundleType;
import org.example.mother.BundleMother;
import org.instancio.Instancio;
import org.instancio.Model;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Select.field;

class BundleTest {

    @Test
    void buildRandomValuedBundle() {
        final Model<Bundle> bundleModel = new BundleMother().buildDefaultInstance();
        final Bundle bundle = Instancio.of(bundleModel)
                .create();

        assertThat(bundle).isNotNull();
        assertThat(bundle.getId()).isNotNull();
        assertThat(bundle.getBundleType()).isNotNull();
    }

    @Test
    void buildLookBundle() {
        final Model<Bundle> bundleModel = new BundleMother().buildDefaultInstance();
        final Bundle bundle = Instancio.of(bundleModel)
                .set(field(Bundle::getBundleType), BundleType.LOOK)
                .create();

        assertThat(bundle).isNotNull();
        assertThat(bundle.getId()).isNotNull();
        assertThat(bundle.getBundleType()).isNotNull();
        assertThat(bundle.isLookType()).isTrue();
    }
}
