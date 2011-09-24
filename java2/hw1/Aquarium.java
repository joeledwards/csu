import java.util.ArrayList;

public class Aquarium 
{
    protected int  capacity = 12;
    protected ArrayList<Fish> occupants;

    public Aquarium()
    {
        occupants = new ArrayList<Fish>(capacity);
    }

    public int addFish(Fish fish)
    {
        if (occupants.size() < capacity) {
            occupants.add(fish);
        }
        return occupants.size();
    }

}

class Aquarius extends Aquarium {
    protected boolean addFish(Fish[] fish)
    {
        if ((occupants.size() + fish.length) < capacity) {
            for (Fish aFish: fish) {
                occupants.add(aFish);
            }
            return true;
        } else {
            return false;
        }
    }
}

class Fish
{
    private boolean hungry = true;

    public void eat()
    {
        hungry = false;
    }
}

