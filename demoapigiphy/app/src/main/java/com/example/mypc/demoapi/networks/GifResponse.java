package com.example.mypc.demoapi.networks;

import java.util.List;

public class GifResponse {
    public List<Data> data;

    public class Data{
        public ImagesJSON images;
        public String title;

        public class ImagesJSON{
            public OriginalJSON original;
            public PreviewGifJSON preview_gif;
            public FixedHeightJSON fixed_height;
            public FixedWidthJSON fixed_width;

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
}
