var lastTime = -1;
var levelFilter = "All";
var filterMode = "AT_LEAST";
var intervalId;
var ordering=-1;

function loadLog() {
	var xmlHttp;
	try {
		// Firefox, Opera 8.0+, Safari
		xmlHttp=new XMLHttpRequest();
  		} 
	catch (e)  {
	    // Internet Explorer
	    try {
			xmlHttp=new ActiveXObject("Msxml2.XMLHTTP");
	      	} 
		catch (e) {
			try {
	        	xmlHttp=new ActiveXObject("Microsoft.XMLHTTP");
	        	}
			catch (e) {
	        	alert("Your browser does not support AJAX!");
	        	return false;
			}
		}
	}
    xmlHttp.onreadystatechange=function() {
		if (xmlHttp.readyState == 4) {
			if (xmlHttp.status == 200) {
				loadXml(xmlHttp.responseXML);
			}
		}
		document.getElementById("log-loading-progress-indicator").style.visibility = "hidden";
	}
    
    document.getElementById("log-loading-progress-indicator").style.visibility = "visible";
    
    xmlHttp.open("GET",contextPath + "/plugins/servlet/log?level="+levelFilter+"&from=" + lastTime ,true);
    lastTime = new Date().getTime();
    xmlHttp.send(null);
}
  
function loadXml(logData) {
	// documentElement always represents the root node
	var x=logData.documentElement;
	var table = document.getElementById('logContents');
	
	for (var i= 0 ; i < x.childNodes.length; i++) {
		var event = x.childNodes[i];
		if (event.nodeType==1) {
			var level = event.getAttribute('level');
			
			if (checkFilters(level)) {
				var row = table.insertRow(ordering);
				row.className=level;
				var date = new Date();
				date.setTime(parseInt(event.getAttribute('timestamp')));
				var timestamp = "" + date.getFullYear() + "-" + pad(date.getMonth() + 1, 2) + "-" + pad(date.getDate(), 2) + " " + pad(date.getHours(),2) + ":" + pad(date.getMinutes(),2) + ":" + pad(date.getSeconds(),2) + "," + pad(date.getTime() % 1000, 3);
				createCell(row, timestamp);
				createCell(row, event.getAttribute('thread'));
				createCell(row, level);
				createCell(row, event.getAttribute('logger'));
				mc = row.insertCell(-1);
				for (var j = 0; j < event.childNodes.length; j++) {
					var node = event.childNodes[j];
					if (node.nodeType == 1) {
						if ("message" == node.nodeName) {
							var text = node.textContent;
							if (text == undefined){
								text = node.text;
							}
							mc.appendChild(document.createTextNode(text));
						} else if ("throwable" == node.nodeName) {
							var text = node.textContent;
							if (text == undefined){
								text = node.text;
							}
							var pre = document.createElement("pre");
							pre.appendChild(document.createTextNode(text));
							mc.appendChild(pre);
						}
					}
				}
			}
		}
	}
}

function createCell(row, value) {
	cell = row.insertCell(-1);
	cell.appendChild(document.createTextNode(value));
	cell.style.verticalAlign = "top";
}

function pad(input, length) {
	var inputString = "" + input;
	var result = inputString;
	var diff = length - inputString.length;
	
	if (diff > 0) {
		for (var i = 0; i < diff; i++) {
		result = "0" + result;
		}
	}
	return result;
}

function setIntervalDelay(ev) {
	ev || (ev = window.event);
	var element = ev.currentTarget || (ev.currentTarget = ev.srcElement);
	delay = element.value;
	clearInterval(intervalId);
	intervalId = window.setInterval('loadLog()',delay*1000);
	reLoad();
}

function setFilterMode(ev) {
	ev || (ev = window.event);
	var element = ev.currentTarget || (ev.currentTarget = ev.srcElement);
	filterMode = element.value;
	reLoad();
}

function setOrdering(ev) {
	ev || (ev = window.event);
	var element = ev.currentTarget || (ev.currentTarget = ev.srcElement);
	ordering = element.value;
	reLoad();
}

function setLevelFilter(ev) {
	ev || (ev = window.event);
	var element = ev.currentTarget || (ev.currentTarget = ev.srcElement);
	levelFilter = element.value;
	reLoad();
}

function reLoad() {
	// Reset to get all events
	lastTime = -1;
	var table = document.getElementById('logContents');
	for (var j = table.rows.length - 1; j >= 0 ; j--) {
		table.deleteRow(j);
	}
	loadLog();
}

function checkFilters(level) {
	if (filterMode == "AT_LEAST") {
		if (levelFilter == "All" || levelFilter == "DEBUG") {
			return true;
		} else if (levelFilter == "INFO") {
			return level == "INFO" || level == "WARN" || level == "ERROR" || level == "FATAL";
		} else if (levelFilter == "WARN") {
			return level == "WARN" || level == "ERROR" || level == "FATAL";
		} else if (levelFilter == "ERROR") {
			return level == "ERROR" || level == "FATAL";
		} else if (levelFilter == "FATAL") {
			return level == "FATAL";
		}
		return false;
	} else if (filterMode == "ONLY") {
		if (levelFilter == "All") {
			return true;
		} else {
			return level == levelFilter;
		}
	}
	return false;
}

