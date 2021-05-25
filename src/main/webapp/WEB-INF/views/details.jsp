<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="f" uri="http://www.springframework.org/tags/form"%>
<html>
<head>
<meta charset="UTF-8">
<title>書籍の詳細｜シアトルライブラリ｜シアトルコンサルティング株式会社</title>
<link href="<c:url value="/resources/css/reset.css" />" rel="stylesheet" type="text/css">
<link href="https://fonts.googleapis.com/css?family=Noto+Sans+JP" rel="stylesheet">
<link href="<c:url value="/resources/css/default.css" />" rel="stylesheet" type="text/css">
<link href="https://use.fontawesome.com/releases/v5.6.1/css/all.css" rel="stylesheet">
<link href="<c:url value="/resources/css/home.css" />" rel="stylesheet" type="text/css">
<link rel="stylesheet" href="resources/css/lightbox.css">
<script src="https://code.jquery.com/jquery-3.2.1.min.js"></script>
<script src="resources/js/lightbox.js" /></script>
<script src="resources/js/userId2.js" /></script>
</head>
<body class="wrapper">
    <header>
        <div class="left">
            <img class="mark" src="resources/img/logo.png" />
            <div class="logo">Seattle Library</div>
        </div>
        <div class="right">
            <ul>
                <li><a href="<%=request.getContextPath()%>/home" class="menu">Home</a></li>
                <li><a href="<%=request.getContextPath()%>/">ログアウト</a></li>
            </ul>
        </div>
    </header>
    <main>
        <h1>書籍の詳細</h1>
        <div class="content_body detail_book_content">
            <div class="content_left">
                <span>書籍の画像</span>
                <div class="book_thumnail">
                    <a href="${bookDetailsInfo.thumbnailUrl}" data-lightbox="image-1"> <c:if test="${empty bookDetailsInfo.thumbnailUrl}">
                            <img class="book_noimg" src="resources/img/noImg.png">
                        </c:if> <c:if test="${!empty bookDetailsInfo.thumbnailUrl}">
                            <img class="book_img" src="${bookDetailsInfo.thumbnailUrl}">
                        </c:if> <%-- <input type="hidden" name="bookId" value="${bookDetailsInfo.bookId}"> --%>
                    </a>
                </div>
                <c:if test="${!empty available}">
                    <div class="error">${available}</div>
                </c:if>
                <c:if test="${!empty unavailable}">
                    <div class="error">${unavailable}</div>
                </c:if>
            </div>
            <div class="content_right">
                <div>
                    <span>書籍名</span>
                    <p>${bookDetailsInfo.title}</p>
                </div>
                <div>
                    <span>著者名</span>
                    <p>${bookDetailsInfo.author}</p>
                </div>
                <div>
                    <span>出版社</span>
                    <p>${bookDetailsInfo.publisher}</p>
                </div>
                <div>
                    <span>出版日</span>
                    <p>${bookDetailsInfo.publishDate}</p>
                </div>
                <div>
                    <span>ISBN</span>
                    <p>${bookDetailsInfo.isbn}</p>
                </div>
                <div>
                    <span>説明文</span>
                    <p>${bookDetailsInfo.comments}</p>
                </div>
            </div>
            <form action="<%=request.getContextPath()%>/reviewBook" method="post" enctype="multipart/form-data" id="data_upload_form">
                <div>
                    <h2>ユーザーレビュー</h2>
                    <br>
                    <div class="reviewContents">
                        <!-- ここにレビュー内容を表示するタグを入れる。 -->
                        <c:if test="${!empty reviewList}">
                            <c:forEach var="reviewInfo" items="${reviewList}">
                                <ul>
                                    <li>ユーザーNo. ${reviewInfo.userId} さん</li>
                                    <li>${reviewInfo.review}</li>
                                </ul>
                            </c:forEach>
                        </c:if>
                        <c:if test="${empty reviewList}">
                            <a>更新ボタンを押してアップデートしてください。</a>
                        </c:if>
                    </div>
                    <br>
                    <!-- 投稿フォームをこれ以降に作成する。同じフォームタグ内にinputとボタンをかく -->
                    <div>
                        <div>
                            <span>レビューを書く</span><span class="care care1">任意</span><br>
                            <textarea name="reviewPosted" cols=40 rows=5 wrap="soft"></textarea>
                        </div>
                        <br>
                        <div class="sentReviewBtn_box">
                            <button type="submit" class="btn_postReview">更新する</button>
                        </div>
                        <!-- 投稿完了メッセージ -->
                        <c:if test="${!empty postCfm}">
                            <div>${postCfm}</div>
                        </c:if>
                        <c:if test="${!empty postError}">
                            <div class="error">${postError}</div>
                        </c:if>
                    </div>
                    <input type="hidden" name="bookId" value="${bookDetailsInfo.bookId}" /> <input type="hidden" name="userId" class="get_userId" value="userIdGet" />
                </div>
            </form>
        </div>
        <div class="edtDelBookBtn_box">
            <c:if test="${!empty unavailable}">
                <form method="post" action="rentBook">
                    <button type="submit" value="${bookDetailsInfo.bookId}" name="bookId" class="btn_rentBook" disabled>借りる</button>
                    <input type="hidden" name="userId" class="get_userId" value="${userIdGet}" />
                </form>
            </c:if>
            <c:if test="${!empty available}">
                <form method="post" action="rentBook">
                    <button type="submit" value="${bookDetailsInfo.bookId}" name="bookId" class="btn_rentBook">借りる</button>
                    <input type="hidden" name="userId" class="get_userId" value="${userIdGet}" />
                </form>
            </c:if>
            <c:if test="${!empty unavailable}">
                <form method="post" action="returnBook">
                    <button type="submit" value="${bookDetailsInfo.bookId}" name="bookId" class="btn_returnBook">返す</button>
                    <input type="hidden" name="userId" class="get_userId" value="${userIdGet}" />
                </form>
            </c:if>
            <c:if test="${!empty available}">
                <form method="post" action="returnBook">
                    <button type="submit" value="${bookDetailsInfo.bookId}" name="bookId" class="btn_returnBook" disabled>返す</button>
                    <input type="hidden" name="userId" class="get_userId" value="${userIdGet}" />
                </form>
            </c:if>
            <c:if test="${!empty available}">
                <form method="post" action="editBook">
                    <button type="submit" value="${bookDetailsInfo.bookId}" name="bookId" class="btn_editBook">編集</button>
                    <input type="hidden" name="userId" class="get_userId" value="${userIdGet}" />
                </form>
            </c:if>
            <c:if test="${!empty unavailable}">
                <form method="post" action="editBook">
                    <button type="submit" value="${bookDetailsInfo.bookId}" name="bookId" class="btn_editBook" disabled>編集</button>
                    <input type="hidden" name="userId" class="get_userId" value="${userIdGet}" />
                </form>
            </c:if>
            <c:if test="${!empty available}">
                <form method="post" action="deleteBook">
                    <button type="submit" value="${bookDetailsInfo.bookId}" name="bookId" class="btn_deleteBook">削除</button>
                </form>
            </c:if>
            <c:if test="${!empty unavailable}">
                <form method="post" action="deleteBook">
                    <button type="submit" value="${bookDetailsInfo.bookId}" name="bookId" class="btn_deleteBook" disabled>削除</button>
                </form>
            </c:if>
        </div>
    </main>
</body>
</html>
