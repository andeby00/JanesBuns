package dk.au.mad22spring.janesbuns;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import dk.au.mad22spring.janesbuns.models.Order;
import dk.au.mad22spring.janesbuns.models.User;

public class OrderDetailsAdapter extends RecyclerView.Adapter<OrderDetailsAdapter.OrderDetailsViewHolder> {

    private List<Order> orderList;
    private Context context;
    private User currentUser;
    int index;

    public OrderDetailsAdapter(Context context, int index) {
        this.context = context;
        this.index = index;
    }

    public void updateUserOrderList(User currentUser) {
        this.currentUser = currentUser;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public OrderDetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_details_item, parent, false);
        return new OrderDetailsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderDetailsViewHolder holder, int position) {
        holder.txtName.setText(currentUser.orders.get(index).creamBuns.get(position).name.toString());
        holder.txtPrice.setText(currentUser.orders.get(index).creamBuns.get(position).price.toString());
        holder.txtQuantity.setText(currentUser.orders.get(index).creamBuns.get(position).amount.toString());
    }

    @Override
    public int getItemCount() {
        return currentUser.orders.get(index).creamBuns.size();
    }

    static class OrderDetailsViewHolder extends RecyclerView.ViewHolder {

        TextView txtName;
        TextView txtPrice;
        TextView txtQuantity;

        public OrderDetailsViewHolder(@NonNull View itemView) {
            super(itemView);

            txtName = itemView.findViewById(R.id.txtOrderDetailsItemName);
            txtPrice = itemView.findViewById(R.id.txtOrderDetailsItemPrice);
            txtQuantity = itemView.findViewById(R.id.txtOrderDetailsItemQuantity);
        }
    }
}
