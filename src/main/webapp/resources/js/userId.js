$(function() {
	var userIdSet = $('#userId').val();

	if (userIdSet.length !== 0) {
		window.sessionStorage.setItem(['userId'], [userIdSet]);
	}
});
