package simulation;

import java.util.*;
import java.util.stream.Collectors;

public class WorldMap extends AbstractWorldMap {
    private static final int ANIMALS_NO = 15, PLANTS_NUMBER = 100;
    private static final int ANIMAL_ENERGY = 22;
    private static final int PLANT_ENERGY = 10;
    private Map <Vector2D, List<Animal>> animalsPosition = new HashMap<>();
    private List<Animal> animals = new ArrayList<>();
    private Map <Vector2D, Plant > plants = new HashMap<>();
    private  int dayNumber = 1;
    private Random random;

    public WorldMap(int width, int height) {
        super(width, height);
        this.random = new Random();
        for (int i = 0; i < ANIMALS_NO; i++)
        {
           addNewAnimal(new Animal(getRandomPosition(), ANIMAL_ENERGY));
        }
        for (int i = 0; i < PLANTS_NUMBER; i++ )
        {
            placePlantOnMap();
        }
    }

    private void placeAnimalOnMap(Animal animal) {
        animalsPosition.computeIfAbsent(animal.getPosition(), pos -> new LinkedList<>()).add(animal);
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
        System.out.println("Today is day number " + dayNumber);
        animalsPosition.clear();
        animals.forEach(animal -> {
            animal.move(MapDirection.values()[this.random.nextInt(MapDirection.values().length)], width, height);
            placeAnimalOnMap(animal);
        });
    }

    public void eat()
    {
        animalsPosition.forEach((position, animals) ->
        {
            if (isOccupiedByPlant(position))
            {
                animals.stream().max(Animal::compareTo).ifPresent(this::eatPlant);
            }
        });

    }

    private  void eatPlant(Animal animal)
    {
        System.out.println("Animal ate plant at position " + animal.getPosition());
        animal.setEnergy(animal.getEnergy() + ANIMAL_ENERGY);
        plants.remove(animal.getPosition());
        placePlantOnMap();
    }

    @Override
    public void atTheEndOfDay()
    {
        dayNumber++;
        animals = animals.stream()
                .map(animal -> animal.ageing())
                .map(animal -> animal.setEnergy(animal.getEnergy() - ANIMAL_ENERGY / 2))
                .filter(animal -> animal.getEnergy() > 0)
                .collect(Collectors.toList());
    }

    @Override
    public void reproduce()
    {
        List<Animal> children = new LinkedList<>();
        animalsPosition.forEach((position, animals) -> {
            List<Animal> parents = animals.stream()
                    .filter(a -> a.getEnergy() > ANIMAL_ENERGY / 2)
                    .sorted(Collections.reverseOrder())
                    .limit(2)
                    .collect(Collectors.toList());
                if (parents.size() == 2)
                {
                    Animal child = new Animal(parents.get(0), parents.get(1));
                    System.out.println("Animal " + child.getAnimalID() + " was born on position " + position );
                    children.add(child);
                }

        }) ;

       children.forEach(this::addNewAnimal);

    }

    private void addNewAnimal(Animal animal)
    {
        animals.add(animal);
        placeAnimalOnMap(animal);
    }
}
