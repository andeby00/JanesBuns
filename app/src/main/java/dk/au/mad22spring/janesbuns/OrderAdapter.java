package dk.au.mad22spring.janesbuns;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import dk.au.mad22spring.janesbuns.models.CreamBun;
import dk.au.mad22spring.janesbuns.models.Order;
import dk.au.mad22spring.janesbuns.models.User;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
    public interface IOrderItemClickedListener {
        void onOrderClicked(int index);
    }

    private IOrderItemClickedListener listener;
    private List<Order> orderList;
    private Context context;
    private User currentUser;

    public OrderAdapter(IOrderItemClickedListener listener, Context context) {
        this.listener = listener;
        this.context = context;
    }

    public void updateOrderList(List<Order> list) {
        orderList = list;
        notifyDataSetChanged();
    }

    public void updateUserOrderList(User currentUser) {
        this.currentUser = currentUser;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item, parent, false);
        return new OrderViewHolder(v, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {

        if(currentUser.isAdmin) {
            StringBuilder tempString = new StringBuilder();

            for (CreamBun creamBun : orderList.get(position).creamBuns) {
                tempString.append(creamBun.amount).append("x ").append(creamBun.name).append(", ");
            }
            tempString.deleteCharAt(tempString.lastIndexOf(","));


            holder.txtName.setText(tempString);
            holder.txtStatus.setText(orderList.get(position).status.toString());
            holder.txtDate.setText(orderList.get(position).date.toString());
            holder.txtTime.setText(orderList.get(position).time.toString());
        } else {
            StringBuilder tempString = new StringBuilder();

            for (CreamBun creamBun : currentUser.orders.get(position).creamBuns) {
                tempString.append(creamBun.amount).append("x ").append(creamBun.name).append(", ");
            }
            tempString.deleteCharAt(tempString.lastIndexOf(","));


            holder.txtName.setText(tempString);
            holder.txtStatus.setText(currentUser.orders.get(position).status.toString());
            holder.txtDate.setText(currentUser.orders.get(position).date.toString());
            holder.txtTime.setText(currentUser.orders.get(position).time.toString());
        }
    }

    @Override
    public int getItemCount() {
        if(currentUser.isAdmin) {
            return orderList.size();
        } else {
            return currentUser.orders.size();
        }
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView txtName;
        TextView txtStatus;
        TextView txtDate;
        TextView txtTime;
        IOrderItemClickedListener listener;

        public OrderViewHolder(@NonNull View itemView, IOrderItemClickedListener orderItemClickedListener) {
            super(itemView);

            txtName = itemView.findViewById(R.id.txtOrderItemName);
            txtStatus = itemView.findViewById(R.id.txtOrderItemStatus);
            txtDate = itemView.findViewById(R.id.txtOrderItemDate);
            txtTime = itemView.findViewById(R.id.txtOrderItemTime);
            listener = orderItemClickedListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onOrderClicked(getBindingAdapterPosition());
        }
    }
}
