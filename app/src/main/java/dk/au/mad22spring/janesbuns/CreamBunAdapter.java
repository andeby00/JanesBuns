package dk.au.mad22spring.janesbuns;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import dk.au.mad22spring.janesbuns.models.CreamBun;

public class CreamBunAdapter extends RecyclerView.Adapter<CreamBunAdapter.CreamBunViewHolder> {
    public interface ICreamBunItemClickedListener {
        void onCreamBunClicked(int index);
    }

    private ICreamBunItemClickedListener listener;
    private List<CreamBun> creamBunList;

    public CreamBunAdapter(ICreamBunItemClickedListener listener) {
        this.listener = listener;
    }

    public void updateCreamBunList(List<CreamBun> list) {
        creamBunList = list;
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
        holder.txtPrice.setText(creamBunList.get(position).price.toString());
        holder.txtAmount.setText(creamBunList.get(position).amount.toString());
        //Glide.with(holder.imgIcon.getContext()).load(creamBunList.get(position).getStrCreamBunThumb()).placeholder(R.drawable.creamBun_placeholder).into(holder.imgIcon);
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
            listener.onCreamBunClicked(getAdapterPosition());
        }
    }
}
