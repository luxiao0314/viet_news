package com.viet.news.core.domain.response

class NewsResponse(var articles: List<ArticlesBean>? = null)
class ArticlesBean(
        var author: String? = null,
        var authorUrl: String? = null,
        var title: String? = null,
        var description: String? = null,
        var url: String? = null,
        var time: String? = null,
        var publishedAt: String? = null,
        var urlToImage: List<ImageEntity>? = null)