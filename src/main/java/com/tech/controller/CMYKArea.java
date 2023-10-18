package com.tech.controller;

public class CMYKArea {
    private String component;
    private double area;
    @Override
    public String toString() {
        return "CMYKArea{" +
                "component='" + component + '\'' +
                ", area=" + area +
                '}';
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        this.area = area;
    }
}