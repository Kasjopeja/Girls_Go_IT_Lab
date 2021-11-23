package simulation;

import java.util.Random;

public class Animal implements Comparable <Animal>  {
	private Vector2D position;

	private int energy;
	private int age = 1;
	private final Genome genome;
	private  int numberOfChildren = 0;
	private final int animalID;
	private static int counter = 0;

	public Animal(Vector2D position, int energy)
	{
		this.position = position;
		this.energy = energy;
		this.animalID = counter++;
		this.genome = new Genome();
	}

	public Animal(Animal mother, Animal father)
	{
		Vector2D move = MapDirection.values()[new Random().nextInt(MapDirection.values().length)].getUnitVector();
		this.position = pbc(mother.getPosition().add(move));
		this.energy = (mother.getEnergy() + father.getEnergy()) / 4;
		this.animalID = counter++;
		this.genome = new Genome(mother.getGenome(), father.getGenome());
		mother.setEnergy(mother.getEnergy() * 3/4);
		father.setEnergy(father.getEnergy() * 3/4);
		mother.increaseNumberOfChildren();
		father.increaseNumberOfChildren();
	}

	public int getNumberOfChildren()
	{
		return numberOfChildren;
	}

	public void increaseNumberOfChildren()
	{
		numberOfChildren++;
	}

	public Genome getGenome() {
		return genome;
	}

	public int getAnimalID() {
		return animalID;
	}

	public Animal setEnergy(int energy)
	{
		this.energy = energy;
		return this;
	}

	public int getEnergy()
	{
		return energy;
	}

	public int getAge() {
		return age;
	}

	public Animal ageing()
	{
		age++;
		return this;
	}

	public Vector2D getPosition() {
		return position;
	}

	public void move(MapDirection direction, int width, int height) {
    
		position = pbc(position.add(direction.getUnitVector()));
		System.out.println("Animal " + animalID + " moves " + direction + ": new position: " + position + " Energy: " + energy + " Age: " + age);
	}

	private Vector2D pbc(Vector2D position) {
		int width = Simulation.getWorldMap().getWidth();
		int height = Simulation.getWorldMap().getHeight();

		if (position.getX() < 0) return position.add(new Vector2D(width, 0));
		if (position.getX() >= width) return position.subtract(new Vector2D(width, 0));
		if (position.getY() < 0) return position.add(new Vector2D(0, height));
		if (position.getY() >= height) return position.subtract(new Vector2D(0, height));

		return position;
	}

	public int compareTo(Animal animal)
	{
		return getEnergy() == animal.getEnergy()
				? getAnimalID() - animal.getEnergy()
				: getEnergy() - animal.getEnergy();

	}
}
