public class ShoppingListAdapter extends ArrayAdapter<Ingredient> {
    private final Context context;
    private final ArrayList<Ingredient> values;

    public ShoppingListAdapter(Context context, ArrayList<Ingredient> values) {
        super(context, R.layout.shopping_list_item, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.shoplist_maincontent, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.shopListMainIngredient);
        textView.setText(values.get(position).getDescription());
        return rowView;
    }


    
}
