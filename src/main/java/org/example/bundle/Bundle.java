package org.example.bundle;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Bundle {

    private String id;

    private BundleType bundleType;

    public boolean isLookType() {
        return BundleType.LOOK.equals(this.bundleType);
    }
}
