package edu.mccneb.codeschool.qraphql.controller;

import edu.mccneb.codeschool.qraphql.model.Author;
import edu.mccneb.codeschool.qraphql.model.Category;
import edu.mccneb.codeschool.qraphql.model.Recipe;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
public class GraphQLController {
    List<Author> authors = new ArrayList<>();
    List<Recipe> recipes = new ArrayList<>();

    @QueryMapping
    public List<Author> getAuthors(){
        return authors;
    }
    @QueryMapping
    public List<Recipe> getRecipes(){
        return recipes;
    }
    @QueryMapping
    public Author getBookById(@Argument UUID authorId){
        return authors.stream().filter(author -> author.getId().toString().equals(authorId.toString())).findFirst().get();
    }
    @QueryMapping
    public Recipe getRecipeById(@Argument UUID recipeId){
        return recipes.stream().filter(recipe -> recipe.getId().toString().equals(recipeId.toString())).findFirst().get();
    }
    @MutationMapping
    public Author createAuthor(@Argument String firstName, @Argument String lastName, @Argument String userName, @Argument String dateOfBirth){
        Author author = new Author();

        author.setId(UUID.randomUUID());
        author.setFirstName(firstName);
        author.setLastName(lastName);
        author.setUserName(userName);
        author.setDateOfBirth(dateOfBirth);
        authors.add(author);
        return author;
    }
    @MutationMapping
    public Recipe createRecipe(@Argument String title, @Argument List<String> ingredients, @Argument List<String> instructions, @Argument UUID authorId, @Argument Category category){
        Recipe recipe = new Recipe();

        recipe.setId(UUID.randomUUID());
        recipe.setTitle(title);
        recipe.setIngredients(ingredients);
        recipe.setInstructions(instructions);
        recipe.setAuthorId(authorId);
        recipe.setCategory(category);
        recipes.add(recipe);
        return recipe;
    }

    @MutationMapping
    public void deleteAuthorById(@Argument UUID authorId){
        Author deleteAuth = authors.stream().filter(author -> author.getId().toString().equals(authorId.toString())).findFirst().get();
        authors.remove(deleteAuth);
    }
    @MutationMapping
    public void deleteRecipeById(@Argument UUID recipeId){
        Recipe deleteRecipe = recipes.stream().filter(recipe -> recipe.getId().toString().equals(recipeId.toString())).findFirst().get();
        recipes.remove(deleteRecipe);
    }

    @SchemaMapping(typeName = "Author", field = "recipe")
    public List<Recipe> getRecipes(Author author){
        return recipes.stream().filter(recipe -> recipe.getAuthorId().toString().equals(author.getId().toString())).collect(Collectors.toList());
    }
    @SchemaMapping(typeName = "Recipe", field = "author")
    public Author getAuthor(Recipe book){
        return authors.stream().filter(author -> author.getId().toString().equals(book.getAuthorId().toString())).findFirst().get();
    }
}
