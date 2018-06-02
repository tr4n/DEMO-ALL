package com.example.mypc.officaligif.networks;

import java.util.List;

public class MediaResponse {
    public List<DataJSON> data;
    public PaginationJSON pagination;
    public MetaJSON meta;

    public class DataJSON {
        public ImagesJSON images;
        public String title;
        public String id;
        public String source_tld;
        public String caption;


        public class ImagesJSON{
            public OriginalJSON original;
            public PreviewGifJSON preview_gif;
            public FixedHeightJSON fixed_height;
            public FixedWidthJSON fixed_width;
            public FixedHeightSmallJSON fixed_height_small;
            public FixedWidthSmallJSON fixed_width_small;
            public FixedWidthStillJSON fixed_width_still;

            public class FixedHeightJSON{
                public String url;
                public String width;
                public String height;


            }
            public class FixedWidthJSON{
                public String url;
                public String width;
                public String height;


            }

            public class FixedWidthSmallJSON{
                public String url;
                public String width;
                public String height;

            }
            public class FixedHeightSmallJSON{
                public String url;
                public String width;
                public String height;

            }
            public class FixedWidthStillJSON{
                public String url;
                public String width;
                public String height;

            }



            public class PreviewGifJSON{
                public String url;
                public String width;
                public String height;

            }
            public class OriginalJSON{
                public String url;
                public String width;
                public String height;


            }
        }
    }

    public class PaginationJSON {
        public int count;
        public int total_count;
    }

    public class MetaJSON {
        public String status;
    }
}
