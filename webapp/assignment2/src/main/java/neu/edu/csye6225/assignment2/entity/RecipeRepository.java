package neu.edu.csye6225.assignment2.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Entity
@Table(name="recipe")
public class RecipeRepository {
    @Id
    private String id;//read-only
    @Temporal(TemporalType.TIMESTAMP)
    private Date created_ts;
    @Temporal(TemporalType.TIMESTAMP)
    private Date updated_ts;
    @NotNull
    private String author_id;
    @NotNull
    private Integer cook_time_in_min;
    @NotNull
    private Integer prep_time_in_min;
    @NotNull
    private Integer total_time_in_min;
    @NotNull
    private String title;
    @NotNull
    private String cusine;
    @NotNull
    private Integer servings;
    @NotNull
    private ArrayList<String> ingredients;

    @NotNull
    @OneToMany(mappedBy = "recipe",cascade=CascadeType.ALL,fetch=FetchType.LAZY)
    //级联保存、更新、删除、刷新;延迟加载。当删除食谱，会级联删除该食谱的所有步骤
    //拥有mappedBy注解的实体类为关系被维护端
    private List<OrderedListRepository> steps;

    @NotNull
    @OneToOne(cascade=CascadeType.ALL)//Recipe 是关系的维护端，当删除 Recipe，会级联删除 nutrition_information
    @JoinColumn(name = "nutrition_information", referencedColumnName = "id")
    private NutritionInformationRepository nutrition_information;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getCreated_ts() {
        return created_ts;
    }

    public void setCreated_ts(Date created_ts) {
        this.created_ts = created_ts;
    }

    public Date getUpdated_ts() {
        return updated_ts;
    }

    public void setUpdated_ts(Date updated_ts) {
        this.updated_ts = updated_ts;
    }

    public String getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(String author_id) {
        this.author_id = author_id;
    }

    public Integer getCook_time_in_min() {
        return cook_time_in_min;
    }

    public void setCook_time_in_min(Integer cook_time_in_min) {
        this.cook_time_in_min = cook_time_in_min;
    }

    public Integer getPrep_time_in_min() {
        return prep_time_in_min;
    }

    public void setPrep_time_in_min(Integer prep_time_in_min) {
        this.prep_time_in_min = prep_time_in_min;
    }

    public Integer getTotal_time_in_min() {
        return total_time_in_min;
    }

    public void setTotal_time_in_min(Integer total_time_in_min) {
        this.total_time_in_min = total_time_in_min;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCusine() {
        return cusine;
    }

    public void setCusine(String cusine) {
        this.cusine = cusine;
    }

    public Integer getServings() {
        return servings;
    }

    public void setServings(Integer servings) {
        this.servings = servings;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<String> ingredients) {
        this.ingredients = ingredients;
    }

    public List<OrderedListRepository> getSteps() {
        return steps;
    }

    public void setSteps(List<OrderedListRepository> steps) {
        this.steps = steps;
    }

    public NutritionInformationRepository getNutrition_information() {
        return nutrition_information;
    }

    public void setNutrition_information(NutritionInformationRepository nutrition_information) {
        this.nutrition_information = nutrition_information;
    }


}
