package com.example.mypc.demoapi.networks;

import java.util.List;

public class GifResponse {
    public List<DataJSON> data;
    public PaginationJSON pagination;
    public MetaJSON meta;

    public class DataJSON {
        public ImagesJSON images;
        public String title;
        public String id;


        public class ImagesJSON{
            public OriginalJSON original;
            public PreviewGifJSON preview_gif;
            public FixedHeightJSON fixed_height;
            public FixedWidthSmallJSON fixed_width_small;

            public class FixedHeightJSON{
                public String url;
                public String width;
                public String height;

            }

            public class FixedWidthSmallJSON{
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
