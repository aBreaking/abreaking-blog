
1. 静态资源全部使用nginx，这个应该现在就可以启用，需要考虑上文件上传及现有的静态资源的位置 该如何放置；
2. http 需要全部 重定向到https，目前这个nignx没有启用http_rewrite_module，通过（./sbin/nginx -V）。好像只有重装nginx才能解决；
3. 