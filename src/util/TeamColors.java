package util;

public enum TeamColors {
    RED_TEAM("Red"), BLUE_TEAM("Blue");

    TeamColors(String color){
        this.color=color;
    }

    private String color;

    public String getColor(){
        return color;
    }
}
