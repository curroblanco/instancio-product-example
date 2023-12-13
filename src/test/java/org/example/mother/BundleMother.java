package org.example.mother;

import org.example.bundle.Bundle;
import org.example.bundle.BundleType;
import org.instancio.Instancio;
import org.instancio.Model;

import java.util.Arrays;
import java.util.UUID;

import static org.instancio.Select.field;

public class BundleMother implements Mother<Model<Bundle>> {
    @Override
    public Model<Bundle> buildDefaultInstance() {
        return Instancio.of(Bundle.class)
                .set(field(Bundle::getId), UUID.randomUUID().toString())
                .set(field(Bundle::getBundleType), Arrays.stream(BundleType.values()).findAny().get())
                .toModel();
    }
}
