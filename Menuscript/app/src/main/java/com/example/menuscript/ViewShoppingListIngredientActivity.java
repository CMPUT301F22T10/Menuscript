public class ViewShoppingListIngredientActivity extends AppCompatActivity{
    private TextView description;
    private TextView amount;
    private TextView unit;
    private TextView category;
    private Ingredient ingredient;
    private DatabaseManager db = new DatabaseManager(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_shoplist_ingredient);

        description = findViewById(R.id.view_shopListIngredientName);
        amount = findViewById(R.id.view_countEditText);
        unit = findViewById(R.id.view_unitEditText);
        category = findViewById(R.id.view_categoryEditText);
        Bundle bundle = getIntent().getExtras();
        description.setText(bundle.getString("NAME"));
        amount.setText(Float.toString(bundle.getFloat("AMOUNT")));
        unit.setText(bundle.getString("UNIT"));
        category.setText(bundle.getString("CATEGORY"));
    }
    
}
