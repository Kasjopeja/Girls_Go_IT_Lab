package simulation;

import java.util.ArrayList;
import java.util.Random;

public class WorldMap extends AbstractWorldMap {
    private static final int ANIMALS_NO = 15, PLANTS_NUMBER = 100;
    private ArrayList<Animal> animals = new ArrayList<>();
    private ArrayList<Plant> plants = new ArrayList<>();
    private Random random;

    public WorldMap(int width, int height) {
        super(width, height);
        this.random = new Random();
        for (int i = 0; i < ANIMALS_NO; i++)
        {
            animals.add(new Animal((getRandomPosition())));
        }
    }

    private  Vector2D getRandomPosition()
    {
        return  new Vector2D(random.nextInt(getWidth()), random.nextInt(getHeight()));
    }

    @Override
    public void run() {
        for (Animal animal : animals) {
            animal.move(MapDirection.values()[this.random.nextInt(MapDirection.values().length)], width, height);
        }
    }
}
