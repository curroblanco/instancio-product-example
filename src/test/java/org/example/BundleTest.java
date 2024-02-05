package org.example;

import org.example.bundle.Bundle;
import org.example.bundle.BundleType;
import org.example.mother.BundleMother;
import org.example.util.BenchMark;
import org.example.util.BenchMarkExtension;
import org.instancio.Instancio;
import org.instancio.Model;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Select.field;

@ExtendWith(BenchMarkExtension.class)
class BundleTest {

    @Test
    @BenchMark
    void buildRandomValuedBundle() throws InterruptedException {
        final Model<Bundle> bundleModel = new BundleMother().buildDefaultInstance();
        final Bundle bundle = Instancio.of(bundleModel)
                .create();

        Thread.sleep(2000);

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
