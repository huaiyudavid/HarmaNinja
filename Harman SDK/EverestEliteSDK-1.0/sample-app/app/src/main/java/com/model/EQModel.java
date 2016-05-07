package com.model;

import com.control.EQSettingManager;

import java.io.Serializable;


public class EQModel implements Serializable {

    private String eqName;

    private int high2, high3, high1;
    private int medium1, medium2, medium3, medium4;
    private int low1, low2, low3;

    public EQModel() {

    }

    public EQModel(EQSettingManager.PresetType eqType) {
        //TODO initiate EQ for predefine preset from array
    }

    public int getHigh2() {
        return high2;
    }

    public void setHigh2(int high2) {
        this.high2 = high2;
    }

    public int getHigh3() {
        return high3;
    }

    public void setHigh3(int high3) {
        this.high3 = high3;
    }

    public int getMedium1() {
        return medium1;
    }

    public void setMedium1(int medium1) {
        this.medium1 = medium1;
    }

    public int getMedium2() {
        return medium2;
    }

    public void setMedium2(int medium2) {
        this.medium2 = medium2;
    }

    public int getMedium3() {
        return medium3;
    }

    public void setMedium3(int medium3) {
        this.medium3 = medium3;
    }

    public int getMedium4() {
        return medium4;
    }

    public void setMedium4(int medium4) {
        this.medium4 = medium4;
    }

    public int getLow1() {
        return low1;
    }

    public void setLow1(int low1) {
        this.low1 = low1;
    }

    public int getLow2() {
        return low2;
    }

    public void setLow2(int low2) {
        this.low2 = low2;
    }


    public int getLow3() {
        return low3;
    }

    public void setLow3(int low3) {
        this.low3 = low3;
    }


    public String getEqName() {
        return eqName;
    }

    public int getHigh1() {
        return high1;
    }

    public void setHigh1(int high1) {
        this.high1 = high1;
    }

    public void setEqName(String eqName) {
        this.eqName = eqName;
    }

    @Override
    public boolean equals(Object o) {
        EQModel temp=(EQModel)o;
        return this.getEqName().equals(temp.getEqName());
    }
}
