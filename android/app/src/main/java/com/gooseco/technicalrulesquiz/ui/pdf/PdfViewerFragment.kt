package com.gooseco.technicalrulesquiz.ui.pdf

import android.graphics.pdf.PdfRenderer
import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gooseco.technicalrulesquiz.R
import com.gooseco.technicalrulesquiz.databinding.FragmentPdfViewerBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import java.io.File
import java.net.URL

class PdfViewerFragment : Fragment() {

    private var _binding: FragmentPdfViewerBinding? = null
    private val binding get() = _binding!!

    private var pdfRenderer: PdfRenderer? = null
    private val renderMutex = Mutex()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPdfViewerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            binding.toolbar.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                topMargin = bars.top
            }
            binding.root.updatePadding(bottom = bars.bottom)
            WindowInsetsCompat.CONSUMED
        }

        val pageNumber = arguments?.getInt("pageNumber", 1) ?: 1

        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        loadPdf(pageNumber)
    }

    private fun loadPdf(pageNumber: Int) {
        val cacheFile = File(requireContext().cacheDir, "ipf_rulebook.pdf")

        if (cacheFile.exists()) {
            openRenderer(cacheFile, pageNumber)
            return
        }

        binding.loadingLayout.visibility = View.VISIBLE
        binding.recyclerView.visibility = View.GONE

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    val url = URL(getString(R.string.rules_book_url))
                    url.openStream().use { input ->
                        cacheFile.outputStream().use { output ->
                            input.copyTo(output)
                        }
                    }
                }
                if (_binding != null) {
                    binding.loadingLayout.visibility = View.GONE
                    openRenderer(cacheFile, pageNumber)
                }
            } catch (e: Exception) {
                cacheFile.delete()
                if (_binding != null) {
                    binding.loadingLayout.visibility = View.GONE
                    binding.errorText.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun openRenderer(file: File, pageNumber: Int) {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val descriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
                val renderer = PdfRenderer(descriptor)
                pdfRenderer = renderer

                val screenWidth = resources.displayMetrics.widthPixels

                val pageHeights = withContext(Dispatchers.Default) {
                    renderMutex.withLock {
                        IntArray(renderer.pageCount) { i ->
                            val page = renderer.openPage(i)
                            val height = (page.height.toFloat() * screenWidth / page.width).toInt()
                            page.close()
                            height
                        }
                    }
                }

                if (_binding == null) return@launch

                val adapter = PdfPageAdapter(
                    renderer = renderer,
                    pageHeights = pageHeights,
                    screenWidth = screenWidth,
                    lifecycleScope = viewLifecycleOwner.lifecycleScope,
                    renderMutex = renderMutex
                )

                val layoutManager = LinearLayoutManager(requireContext())
                binding.recyclerView.layoutManager = layoutManager
                binding.recyclerView.adapter = adapter

                val targetPosition = (pageNumber - 1).coerceIn(0, renderer.pageCount - 1)
                binding.recyclerView.scrollToPosition(targetPosition)

                binding.recyclerView.visibility = View.VISIBLE
                binding.pageIndicator.visibility = View.VISIBLE
                binding.pageIndicator.text = getString(
                    R.string.page_indicator,
                    targetPosition + 1,
                    renderer.pageCount
                )

                binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        val firstVisible = layoutManager.findFirstVisibleItemPosition()
                        if (firstVisible != RecyclerView.NO_POSITION && _binding != null) {
                            binding.pageIndicator.text = getString(
                                R.string.page_indicator,
                                firstVisible + 1,
                                renderer.pageCount
                            )
                        }
                    }
                })

            } catch (e: Exception) {
                if (_binding != null) {
                    binding.errorText.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onDestroyView() {
        binding.recyclerView.adapter = null
        pdfRenderer?.close()
        pdfRenderer = null
        super.onDestroyView()
        _binding = null
    }
}
