package com.gooseco.technicalrulesquiz.ui.pdf

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.pdf.PdfRenderer
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

class PdfPageAdapter(
    private val renderer: PdfRenderer,
    private val pageHeights: IntArray,
    private val screenWidth: Int,
    private val lifecycleScope: CoroutineScope,
    private val renderMutex: Mutex
) : RecyclerView.Adapter<PdfPageAdapter.ViewHolder>() {

    class ViewHolder(val imageView: ZoomableImageView) : RecyclerView.ViewHolder(imageView) {
        var renderJob: Job? = null
    }

    override fun getItemCount(): Int = renderer.pageCount

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val imageView = ZoomableImageView(parent.context).apply {
            layoutParams = RecyclerView.LayoutParams(MATCH_PARENT, 0)
        }
        return ViewHolder(imageView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.renderJob?.cancel()

        val params = holder.imageView.layoutParams as RecyclerView.LayoutParams
        params.height = pageHeights[position]
        holder.imageView.layoutParams = params

        holder.renderJob = lifecycleScope.launch {
            val bitmap = withContext(Dispatchers.Default) {
                try {
                    renderMutex.withLock {
                        val page = renderer.openPage(position)
                        val bmp = Bitmap.createBitmap(screenWidth, pageHeights[position], Bitmap.Config.ARGB_8888)
                        bmp.eraseColor(Color.WHITE)
                        page.render(bmp, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
                        page.close()
                        bmp
                    }
                } catch (e: Exception) {
                    null
                }
            }

            if (holder.bindingAdapterPosition == position && bitmap != null) {
                holder.imageView.setImageBitmap(bitmap)
            }
        }
    }

    override fun onViewRecycled(holder: ViewHolder) {
        super.onViewRecycled(holder)
        holder.renderJob?.cancel()
        holder.renderJob = null
    }
}
