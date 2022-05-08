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

public class OrderDetailsAdminAdapter extends RecyclerView.Adapter<OrderDetailsAdminAdapter.OrderDetailsAdminViewHolder> {

    private List<Order> orderList;
    int index;

    public OrderDetailsAdminAdapter(int index) {
        this.index = index;
    }

    public void updateOrderList(List<Order> list) {
        orderList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public OrderDetailsAdminViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_details_admin_item, parent, false);
        return new OrderDetailsAdminViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderDetailsAdminViewHolder holder, int position) {
        holder.txtName.setText(orderList.get(index).creamBuns.get(position).name.toString());
        holder.txtPrice.setText(orderList.get(index).creamBuns.get(position).price.toString());
        holder.txtQuantity.setText(orderList.get(index).creamBuns.get(position).amount.toString());
    }

    @Override
    public int getItemCount() {
        return orderList.get(index).creamBuns.size();
    }

    static class OrderDetailsAdminViewHolder extends RecyclerView.ViewHolder {

        TextView txtName;
        TextView txtPrice;
        TextView txtQuantity;

        public OrderDetailsAdminViewHolder(@NonNull View itemView) {
            super(itemView);

            txtName = itemView.findViewById(R.id.txtOrderDetailsAdminItemName);
            txtPrice = itemView.findViewById(R.id.txtOrderDetailsAdminItemPrice);
            txtQuantity = itemView.findViewById(R.id.txtOrderDetailsAdminItemQuantity);
        }
    }
}
