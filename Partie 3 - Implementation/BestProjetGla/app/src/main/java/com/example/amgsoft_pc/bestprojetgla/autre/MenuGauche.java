package com.example.amgsoft_pc.bestprojetgla.autre;

public class MenuGauche {

    private int imgId, imgId1, imgId2;
    private String title, abbes;
    private int type;

    public MenuGauche(int imgId, String title) {
        this.imgId = imgId;
        this.title = title;
    }

    public MenuGauche(int imgId, String title, String abbes) {
        this.imgId = imgId;
        this.title = title;
        this.abbes = abbes;
    }

    public MenuGauche(int imgId, int imgId1, String abbes, int imgId2) {
        this.imgId = imgId;
        this.imgId1 = imgId1;
        this.abbes = abbes;
        this.imgId2 = imgId2;
    }

    public MenuGauche(int imgId, String title, String abbes, int type) {
        this.imgId = imgId;
        this.title = title;
        this.abbes = abbes;
        this.type = type;
    }

    public MenuGauche(int imgId, String title, int type) {
        this.imgId = imgId;
        this.title = title;
        this.type = type;
    }

    public MenuGauche(String title, int type) {
        this.title = title;
        this.type = type;
    }

    public MenuGauche(String title) {
        this.title = title;

    }

    public MenuGauche(int imgId, int imgId1, int type) {
        this.imgId = imgId;
        this.imgId1 = imgId1;
        this.type = type;
    }

    public int getImgId2() {

        return imgId2;
    }

    public void setImgId2(int imgId2) {

        this.imgId2 = imgId2;
    }

    public int getImgId1() {

        return imgId1;
    }

    public void setImgId1(int imgId1) {

        this.imgId1 = imgId1;
    }

    public int getImgId() {

        return imgId;
    }

    public void setImgId(int imgId) {

        this.imgId = imgId;
    }

    public String getTitle() {
        return title;

    }

    public void setTitle(String title) {

        this.title = title;
    }

    public String getabbes() {
        return abbes;
    }


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }


}
