package com.example.mypc.demosuper.models;

import java.io.Serializable;
import java.util.List;

public class DataPassing implements Serializable{
    public GifModel gifModel;
    public FixedHeightGIFModel fixedHeightGIFModel;
    public List<FixedHeightGIFModel> fixedHeightGIFModelList ;
    public boolean isVertical;

    public DataPassing(GifModel gifModel, List<FixedHeightGIFModel> fixedHeightGIFModelList) {
        this.gifModel = gifModel;
        this.fixedHeightGIFModelList = fixedHeightGIFModelList;
        this.isVertical = true;
    }
    public DataPassing(FixedHeightGIFModel fixedHeightGIFModel, List<FixedHeightGIFModel> fixedHeightGIFModelList) {
        this.fixedHeightGIFModel = fixedHeightGIFModel;
        this.fixedHeightGIFModelList = fixedHeightGIFModelList;
        this.isVertical = false;
    }
}
