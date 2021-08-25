package com.dristi.kharcha;

public class Categories_item {

    private String name;

    private int image;

    public Categories_item(String cname, int cimage){
        name = cname;
        image = cimage;
    }

    public String getName() {
        return name;
    }

    public int getImage(){
        return image;
    }
}
