public class ShoppingListActivity extends AppCompatActivity{
    ListView shoppingList;
    ShoppingListAdapter shoppingAdapter;
    private ActivityResultLauncher<Intent> activityResultLauncher;
    StoredIngredientListAdapter ingredientAdapter;
    DatabaseManager db = new DatabaseManager(this);
    private final String descriptionFieldStr = "description";
    private final String amountFieldStr = "amount";
    private final String unitFieldStr = "unit";
    private final String categoryFieldStr = "category";
    ArrayList<Ingredient> shoppingItems;
    private FirebaseFirestore databaseInstance;
    private CollectionReference mealPlanCollection;
    private CollectionReference storedIngredientCollection;
    Ingredient clickedShoppingItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shoplist_main);
        shoppingList = findViewById(R.id.shopListMainIngredients);
        databaseInstance = FirebaseFirestore.getInstance();
        shoppingItems = new ArrayList<>();
        shoppingAdapter = new ShoppingListAdapter(this, shoppingItems);
        shoppingList.setAdapter(shoppingAdapter);
        ingredientAdapter = new StoredIngredientListAdapter(this, shoppingItems);
        ingredientList.setAdapter(ingredientAdapter);
        mealPlanCollection = databaseInstance.collection("MealPlanIngredients");
        storedIngredientCollection = databaseInstance.collection("StoredIngredients");
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode()!= null) {
                Intent data = result.getData();
                String description = intent.getStringExtra("description");
                float amount = intent.getFloatExtra("amount", 0.0f);
                String unit = intent.getStringExtra("unit");
                String date = intent.getStringExtra("date");
                String category = intent.getStringExtra("category");
                String location = intent.getStringExtra("location");
                if (result.getResultCode() == 400) {
                    StoredIngredient newIngredient = new StoredIngredient(description, amount, unit, category, date, location);
                    db.addStoredIngredient(newIngredient);
                    ingredients.add(newIngredient);
                    ingredientAdapter.notifyDataSetChanged();
                    Ingredient shoppingItem = new Ingredient(description, amount, unit, category);
                    shoppingItems.add(shoppingItem);
                    shoppingAdapter.notifyDataSetChanged();
                    db.addShoppingItem(shoppingItem);
                }
            }
        });
        FloatingActionButton fab = findViewById(R.id.shopListButton);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(ShoppingListActivity.this, AddIngredientActivity.class);
            activityResultLauncher.launch(intent);
        });
        shoppingList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(ShopListActivity.this, ViewShopListIngredientActivity.class);
                clickedShoppingItem = shoppingAdapter.getItem(i);
                intent.putExtra("NAME", clickedShoppingItem.getDescription());
                intent.putExtra("AMOUNT", clickedShoppingItem.getAmount());
                intent.putExtra("UNIT", clickedShoppingItem.getUnit());
                intent.putExtra("CATEGORY", clickedShoppingItem.getCategory());
                startActivity(intent);
            }    
        });

        shoppingList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                clickedShoppingItem = shoppingAdapter.getItem(i);
                shoppingItems.remove(clickedShoppingItem);
                shoppingAdapter.notifyDataSetChanged();
                db.deleteShoppingItem(clickedShoppingItem);
                return true;
            }
        });

        ingredientList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(ShopListActivity.this, ViewStoredIngredientActivity.class);
                clickedIngredient = ingredientAdapter.getItem(i);
                intent.putExtra("NAME", clickedIngredient.getDescription());
                intent.putExtra("AMOUNT", clickedIngredient.getAmount());
                intent.putExtra("UNIT", clickedIngredient.getUnit());
                intent.putExtra("CATEGORY", clickedIngredient.getCategory());
                intent.putExtra("DATE", clickedIngredient.getDate());
                intent.putExtra("LOCATION", clickedIngredient.getLocation());
                startActivity(intent);
            }
        });


        mealPlanCollection.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.w("TAG", "Listen failed.", error);
                    return;
                }
                for (DocumentChange dc : value.getDocumentChanges()) {
                    switch (dc.getType()) {
                        case ADDED:
                            Log.d("TAG", "New meal plan ingredient: " + dc.getDocument().getData());
                            String description = dc.getDocument().getString(descriptionFieldStr);
                            float amount = dc.getDocument().getFloat(amountFieldStr);
                            String unit = dc.getDocument().getString(unitFieldStr);
                            String category = dc.getDocument().getString(categoryFieldStr);
                            Ingredient shoppingItem = new Ingredient(description, amount, unit, category);
                            shoppingItems.add(shoppingItem);
                            shoppingAdapter.notifyDataSetChanged();
                            db.addShoppingItem(shoppingItem);
                            break;
                        case MODIFIED:
                            Log.d("TAG", "Modified meal plan ingredient: " + dc.getDocument().getData());
                            break;
                        case REMOVED:
                            Log.d("TAG", "Removed meal plan ingredient: " + dc.getDocument().getData());
                            break;
                    }
                }
            }
        });

        storedIngredientCollection.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.w("TAG", "Listen failed.", error);
                    return;
                }
                for (DocumentChange dc : value.getDocumentChanges()) {
                    switch (dc.getType()) {
                        case ADDED:
                            Log.d("TAG", "New stored ingredient: " + dc.getDocument().getData());
                            String description = dc.getDocument().getString(descriptionFieldStr);
                            float amount = dc.getDocument().getFloat(amountFieldStr);
                            String unit = dc.getDocument().getString(unitFieldStr);
                            String category = dc.getDocument().getString(categoryFieldStr);
                            String date = dc.getDocument().getString(dateFieldStr);
                            String location = dc.getDocument().getString(locationFieldStr);
                            StoredIngredient newIngredient = new StoredIngredient(description, amount, unit, category, date, location);
                            ingredients.add(newIngredient);
                            ingredientAdapter.notifyDataSetChanged();
                            db.addStoredIngredient(newIngredient);
                            break;
                        case MODIFIED:
                            Log.d("TAG", "Modified stored ingredient: " + dc.getDocument().getData());
                            break;
                        case REMOVED:
                            Log.d("TAG", "Removed stored ingredient: " + dc.getDocument().getData());
                            break;
                    }
                }
            }
        });
    }    
    
}
