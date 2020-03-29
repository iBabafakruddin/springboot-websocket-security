var chatClient = null;



function login() {
	var chatSocket = new SockJS('/chat-websocket');
	chatClient = Stomp.over(chatSocket);
	chatClient.connect({}, function(frame) {
		console.log('Connected: ' + frame);
		var user = frame.headers["user-name"];
		console.log("user: "+user)
		$("#userNameSpan").text(user);
		chatClient.subscribe('/topic/chat', function(replay) {
			// showGreeting(JSON.parse(greeting.body).content);
			console.log("replay : " + replay.body)
			onChatMessage(replay.body);
		});

		chatClient.subscribe('/user/chat/private', function(replay) {
			// showGreeting(JSON.parse(greeting.body).content);
			console.log("replay : " + replay.body)
			onChatMessage(replay.body);
		});
	});
}

function logout() {
    if (chatClient !== null) {
    	chatClient.disconnect();
    	chatClient = null;
    }
    console.log("Disconnected");
}

function sendChatMessage() {
	  let text = $("#chatMessage").val();
	  let to = $("#toUser").val() ;
	  let from = $("#userNameSpan").text();
	  let message = {from, to, text};
	  const messageStr = JSON.stringify(message);
	  console.log("sendChatMessage : "+messageStr)
	  if(to !== "All"){
		  chatClient.send("/user/"+to+"/chat/private", {},messageStr);
	  } else {
		  chatClient.send("/app/sendchat", {},messageStr);
	  }

}

function onChatMessage(replayStr){
	const message = JSON.parse(replayStr);
	let messageElem = "<div> " +
			"<span> " +message.from +" {msg_type}: </span>"+
			"<span> " +message.text +" </span>"+
			"</div>"
	let prefix = message.to !== "All"  ? " (private chat)" : "(group chat)" ;
	messageElem = messageElem.replace("{msg_type}", prefix)
	console.log("messageElem : "+messageElem);
	$('#messages').prepend(messageElem);
}

function toggleLoginControlls(){
	if(chatClient){
		$("#loginDiv").hide();
		$("#logoutDiv").show ();
		// var userName = $("#userName").val();

	}
	if(!chatClient){
		$("#loginDiv").show();
		$("#logoutDiv").hide ();
		$("#userNameSpan").text("")
	}
}


$(function () {
    $( "#sendChatMessage" ).click(function() { sendChatMessage(); });
    $( "#login" ).click(function() {
    	login();
    	toggleLoginControlls();
    });
    $("#logout").click(function() {
    	logout();
    	toggleLoginControlls();
    });
    toggleLoginControlls();
});