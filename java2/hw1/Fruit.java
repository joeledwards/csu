import java.awt.Image;

public class Fruit
{
    public static void main(String argv[])
    {
        GenericFruit gf = new Apple();
        System.out.printf("Avg Calories: %f\n", gf.averageCalories());
    }
}

class GenericFruit
{
    protected float calorieContent;
    protected float avgWeight;
    protected float caloriesPerGram = (float)20;
    protected float servingSizeGrams = (float)5;
    String varietyName;

    public void setCalorieContent(float f)
    {
        calorieContent = f;
    }

    public float averageCalories()
    {
        return caloriesPerGram * servingSizeGrams;
    }
}

class Apple extends GenericFruit
{
    Image picture;

    public float averageCalories()
    {
        return 120;
    }
/*
    protected float setCalorieContent(String s)
    {
        calorieContent = Float.parseFloat(s);
        return calorieContent;
    }
//*/

/*
    protected void setCalorieContent(float x)
    {
        calorieContent = x;
    }
*/

    public void setCalorieContent(double d)
    {
        calorieContent = (float)d;
    }

//*
    public void setCalorieContent(String s)
        throws NumberFormatException
    {
        calorieContent = Float.parseFloat(s);
    }
//*/
}

