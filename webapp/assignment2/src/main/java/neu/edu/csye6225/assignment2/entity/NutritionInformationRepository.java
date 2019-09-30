package neu.edu.csye6225.assignment2.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "nutritionInformation")
public class NutritionInformationRepository {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    private Integer calories;
    @NotNull
    private float cholesterol_in_mg;
    @NotNull
    private Integer sodium_in_mg;
    @NotNull
    private float carbohydrates_in_grams;
    @NotNull
    private float protein_in_grams;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCalories() {
        return calories;
    }

    public void setCalories(Integer calories) {
        this.calories = calories;
    }

    public float getCholesterol_in_mg() {
        return cholesterol_in_mg;
    }

    public void setCholesterol_in_mg(float cholesterol_in_mg) {
        this.cholesterol_in_mg = cholesterol_in_mg;
    }

    public Integer getSodium_in_mg() {
        return sodium_in_mg;
    }

    public void setSodium_in_mg(Integer sodium_in_mg) {
        this.sodium_in_mg = sodium_in_mg;
    }

    public float getCarbohydrates_in_grams() {
        return carbohydrates_in_grams;
    }

    public void setCarbohydrates_in_grams(float carbohydrates_in_grams) {
        this.carbohydrates_in_grams = carbohydrates_in_grams;
    }

    public float getProtein_in_grams() {
        return protein_in_grams;
    }

    public void setProtein_in_grams(float protein_in_grams) {
        this.protein_in_grams = protein_in_grams;
    }

}
