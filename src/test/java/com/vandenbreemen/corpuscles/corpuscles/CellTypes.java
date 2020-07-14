package com.vandenbreemen.corpuscles.corpuscles;

/*
RESERVED BITS:
0-2 (first 3 bits) -- Cell Types
3-7 -- Configurations for individual cell types
 */

/**
 * Bit positions for configuring cell types.
 */
public class CellTypes {

    /**
     * Coupler byte position.  Coupler cells connect cells to each other
     */
    public static final int COUPLER = 0;

    /**
     * Cell that receives activation from a coupler cell when that coupler is on
     */
    public static final int COUPLER_ENDPOINT = 1;
    public static final int INHIBITOR = 2;

    private CellTypes(){}

    public enum CouplerTypes {

        HORIZONTAL(3),
        VERTICAL(4),
        FWD_SLASH(5)
        ;

        public final int position;

        private CouplerTypes(int position) {
            this.position = position;
        }

        public static class CouplerActivations {
            public static final int FIRST_3 = 6;
            public static final int FIRST_4 = 7;
        }

    }

    public enum InhibitorTypes {

        TwoCells(3),
        FourCells(4),

        ;

        public final int position;

        private InhibitorTypes(int position) {
            this.position = position;
        }

    }

    public enum PulseTypes {
        TwoEpochs(3),
        ThreeEpochs(4);

        public final int position;
        PulseTypes(int position) {
            this.position = position;
        }
    }
}
