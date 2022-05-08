package dk.au.mad22spring.janesbuns;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;


import dk.au.mad22spring.janesbuns.models.CreamBun;

public class CreamBunAdapter extends RecyclerView.Adapter<CreamBunAdapter.CreamBunViewHolder> {
    public interface ICreamBunItemClickedListener {
        void onCreamBunClicked(int index);
    }

    StorageReference storageRef;
    private ICreamBunItemClickedListener listener;
    private List<CreamBun> creamBunList;
    private Context context;

    public CreamBunAdapter(ICreamBunItemClickedListener listener, Context context) {
        this.listener = listener;
        this.context = context;
        storageRef = FirebaseStorage.getInstance().getReference();
    }

    public void updateCreamBunList(List<CreamBun> list, boolean isAdmin) {
        creamBunList = list;
        if(isAdmin) creamBunList.add(new CreamBun("plus_sign.png"));
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CreamBunViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cream_bun_item, parent, false);
        return new CreamBunViewHolder(v, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull CreamBunViewHolder holder, int position) {
        holder.txtName.setText(creamBunList.get(position).name);
        if(creamBunList.get(position).amount > 0) holder.txtAmount.setText(creamBunList.get(position).amount.toString() + " " + context.getString(R.string.quantity));//R.string.txtCreamBunItemQty
        if(creamBunList.get(position).price > 0) holder.txtPrice.setText(creamBunList.get(position).price.toString() + " " + context.getString(R.string.price));
        storageRef.child(creamBunList.get(position).uri).getDownloadUrl().addOnCompleteListener(task -> {
            Glide.with(holder.imgImage.getContext()).load(task.getResult()).placeholder(R.drawable.creambun_placeholder).into(holder.imgImage);
        });
    }

    @Override
    public int getItemCount() {
        return creamBunList.size();
    }

    class CreamBunViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imgImage;
        TextView txtName, txtPrice, txtAmount;
        ICreamBunItemClickedListener listener;

        public CreamBunViewHolder(@NonNull View itemView, ICreamBunItemClickedListener creamBunItemClickedListener) {
            super(itemView);

            imgImage = itemView.findViewById(R.id.imgCreamBunItemImage);
            txtName = itemView.findViewById(R.id.txtCreamBunItemName);
            txtPrice = itemView.findViewById(R.id.txtCreamBunItemPrice);
            txtAmount = itemView.findViewById(R.id.txtCreamBunItemAmount);
            listener = creamBunItemClickedListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int i = getBindingAdapterPosition();
            if(i == creamBunList.size() - 1)
                listener.onCreamBunClicked(-1);

            listener.onCreamBunClicked(i);
        }
    }
}
