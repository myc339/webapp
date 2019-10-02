package neu.edu.csye6225.assignment2.entity;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "orderedList")
public class OrderedListRepository {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    @JSONField(serialize = false)
    private Integer id;

    @NotNull
    @Min(1)
    private Integer position;
    @NotNull
    private String items;

    @ManyToOne(cascade={CascadeType.MERGE,CascadeType.REFRESH},optional=false)//可选属性optional=false,表示recipe不能为空。删除步骤，不影响食谱
    @JoinColumn(name="recipe_id")//设置在orderedList表中的关联字段(外键)
    @JSONField(serialize = false)
    private RecipeRepository recipe;//所属recipe

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }

    public RecipeRepository getRecipe() {
        return recipe;
    }

    public void setRecipe(RecipeRepository recipe) {
        this.recipe = recipe;
    }
}
