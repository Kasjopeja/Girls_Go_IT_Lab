package simulation;

public class Plant {
    private final Vector2D position;

   Plant(Vector2D position)
   {
       this.position = position;
   }

   public Vector2D gotPosition()
   {
       return  position;
   }

}
