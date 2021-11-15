package simulation;

import java.util.*;

public class WorldMap extends AbstractWorldMap {
    private static final int ANIMALS_NO = 15, PLANTS_NUMBER = 100;
    private Map <Vector2D, List<Animal>> animalsPosition = new HashMap<>();
    private ArrayList<Animal> animals = new ArrayList<>();
    private Map <Vector2D, Plant > plants = new HashMap<>();
    private Random random;

    public WorldMap(int width, int height) {
        super(width, height);
        this.random = new Random();
        for (int i = 0; i < ANIMALS_NO; i++)
        {
            Animal animal = new Animal(getRandomPosition());
            animals.add(animal);
            placePlantOnMap();
        }
        for (int i = 0; i < PLANTS_NUMBER; i++ )
        {
            placePlantOnMap();
        }
    }

    private void placeAnimalOnMap(Animal animal)
    {
        List<Animal> animalsAtPosition = animalsPosition.get(animal.getPosition());
        if (animalsAtPosition == null)
        {
            animalsAtPosition = new LinkedList<>();
            animalsPosition.put(animal.getPosition(), animalsAtPosition);
        }

        animalsAtPosition.add(animal);
    }

    private void placePlantOnMap()
    {
        Vector2D position = getRandomPosition();
        while (isOccupiedByPlant(position)) position = getRandomPosition();
        plants.put(position, new Plant(position));
    }

    private  boolean isOccupiedByPlant(Vector2D position)
    {
      return  getPlantAtPosition(position) != null;
    }

    private  Plant getPlantAtPosition(Vector2D position)
    {
      return  plants.get(position);
    }

    private  Vector2D getRandomPosition()
    {
        return  new Vector2D(random.nextInt(getWidth()), random.nextInt(getHeight()));
    }

    @Override
    public void run() {
        animalsPosition.clear();
        for (Animal animal : animals) {
            animal.move(MapDirection.values()[this.random.nextInt(MapDirection.values().length)], width, height);
            placeAnimalOnMap(animal);
        }
    }

    public void eat()
    {
        for (Animal animal : animals)
        {
            if (isOccupiedByPlant(animal.getPosition()))
            {
                System.out.println("Animal ate plant at position " + animal.getPosition());
                plants.remove(animal.getPosition());
                placePlantOnMap();
            }
        }
    }


}
