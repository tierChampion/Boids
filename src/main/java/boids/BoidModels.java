package boids;

public enum BoidModels {

    CONE,
    PYRAMID,
    PLANE;

    public static String fileName(BoidModels modelType) {

        switch(modelType) {
            case PYRAMID -> {return "pyramid.obj";}
            case PLANE -> {return "paperPlane.obj";}
            default -> {return "cone.obj";}
        }
    }

}
