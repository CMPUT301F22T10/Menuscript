public class ShoppingListActivity extends AppCompatActivity{
    ListView shoppingList;
    ShoppingListAdapter shoppingAdapter;
    private ActivityResultLauncher<Intent> activityResultLauncher;
    DatabaseManager db = new DatabaseManager(this);
    private final String descriptionFieldStr = "description";
    private final String amountFieldStr = "amount";
    private final String unitFieldStr = "unit";
    private final String categoryFieldStr = "category";
    private final String dateFieldStr = "date";
    private final String locationFieldStr = "location";
    ArrayList<StoredIngredient> ingredients;
    private FirebaseFirestore databaseInstance;
    private CollectionReference collectionReference;
    ShoppingItem clickedShoppingItem;
    StoredIngredient clickedIngredient;
    StoredIngredientListAdapter ingredientAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_activity);
        shoppingList = findViewById(R.id.shopListMainIngredients);
        databaseInstance = FirebaseFirestore.getInstance();
        collectionReference = databaseInstance.collection("ShoppingItems");
        //shoppingItems = new ArrayList<>();
        //shoppingAdapter = new ShoppingListAdapter(this, shoppingItems);
        //shoppingList.setAdapter(shoppingAdapter);
        ingredients = new ArrayList<>();

        ingredientAdapter = new StoredIngredientListAdapter(this, ingredients);

        ingredientList.setAdapter(ingredientAdapter);
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                Intent data = result.getData();
                if (data != null) {
                    String description = data.getStringExtra("description");
                    String amount = data.getStringExtra("amount", 0.0f);
                    String unit = data.getStringExtra("unit");
                    String category = data.getStringExtra("category");
                    String date = data.getStringExtra("date");
                    String location = data.getStringExtra("location");
                    if (description != null && amount != null && unit != null && category != null && date != null && location != null) {
                        StoredIngredient newIngredient = new StoredIngredient(description, amount, unit, category, date, location);
                        db.addStoredIngredient(newIngredient);
                        ingredients.add(newIngredient);
                        //ShoppingItem shoppingItem = new ShoppingItem(description, amount, unit, category, date, location);
                        //shoppingItems.add(shoppingItem);
                        //shoppingAdapter.notifyDataSetChanged();
                        //db.addShoppingItem(shoppingItem);
                    }
                }
            }
        });
        FloatingActionButton fab = findViewById(R.id.shopListButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShoppingListActivity.this, AddIngredientActivity.class);
                activityResultLauncher.launch(intent);
            }
        });
        shoppingList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), ViewIngredientActivity.class);
                clickedIngredient = (StoredIngredient) ingredientAdapter.getItem(i);
                intent.putExtra("INGREDIENT", clickedIngredient);
                activityResultLauncher.launch(intent);
            }    
        });


    
}
