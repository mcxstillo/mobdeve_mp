package com.mobdeve.castillo.recipe_finder;

public class Steps {
    public int stepnum;
    public String step_desc;

    public Steps(){

    }
//    public Steps(int stepnum, String step_desc) {
//        this.stepnum = stepnum;
//        this.step_desc = step_desc;
//    }

    public int getStepnum() {
        return stepnum;
    }

    public void setStepnum(int stepnum) {
        this.stepnum = stepnum;
    }

    public String getStep_desc() {
        return step_desc;
    }

    public void setStep_desc(String step_desc) {
        this.step_desc = step_desc;
    }
}
