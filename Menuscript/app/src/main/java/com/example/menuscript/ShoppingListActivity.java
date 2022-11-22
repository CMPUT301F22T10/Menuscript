public class ShoppingListActivity extends AppCompatActivity{
    ListView shoppingList;
    ShoppingListAdapter shoppingAdapter;
    private ActivityResultLauncher<Intent> activityResultLauncher;
    DatabaseManager db = new DatabaseManager(this);
    private final String descriptionFieldStr = "description";
    private final String amountFieldStr = "amount";
    private final String unitFieldStr = "unit";
    private final String categoryFieldStr = "category";
    ArrayList<ShoppingItem> shoppingItems;
    private FirebaseFirestore databaseInstance;
    private CollectionReference collectionReference;
    ShoppingItem clickedShoppingItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_activity);
        shoppingList = findViewById(R.id.item_list);
        databaseInstance = FirebaseFirestore.getInstance();
        collectionReference = databaseInstance.collection("ShoppingItems");
        shoppingItems = new ArrayList<>();
        shoppingAdapter = new ShoppingListAdapter(this, shoppingItems);
        shoppingList.setAdapter(shoppingAdapter);
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
                    ShoppingItem shoppingItem = new ShoppingItem(description, amount, unit, category);
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
        shoppingList.setOnItemClickListener((parent, view, position, id) -> {
            clickedShoppingItem = shoppingItems.get(position);
            Intent intent = new Intent(ShoppingListActivity.this, AddShoppingItemActivity.class);
            intent.putExtra(descriptionFieldStr, clickedShoppingItem.getDescription());
            intent.putExtra(amountFieldStr, clickedShoppingItem.getAmount());
            intent.putExtra(unitFieldStr, clickedShoppingItem.getUnit());
            intent.putExtra(categoryFieldStr, clickedShoppingItem.getCategory());
            intent.putExtra(dateFieldStr, clickedShoppingItem.getDate());
            intent.putExtra(locationFieldStr, clickedShoppingItem.getLocation());
            activityResultLauncher.launch(intent);
        });
        shoppingList.setOnItemLongClickListener((parent, view, position, id) -> {
            clickedShoppingItem = shoppingItems.get(position);
            db.deleteShoppingItem(clickedShoppingItem);
            shoppingItems.remove(position);
            shoppingAdapter.notifyDataSetChanged();
            return true;
        });


    
}
