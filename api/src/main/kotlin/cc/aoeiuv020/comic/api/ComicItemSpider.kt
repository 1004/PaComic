package cc.aoeiuv020.comic.api

/**
 * Created by AoEiuV020 on 17-5-31.
 */
abstract class ComicItemSpider : Spider() {
    abstract val name: String
    abstract val imgUrl: String
    abstract val comicDetailUrl: String
    abstract val comicDetail: ComicDetailSpider
}