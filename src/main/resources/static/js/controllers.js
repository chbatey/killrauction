var auctionControllers = angular.module('auctionControllers', []);

auctionControllers.controller('AuctionListController', ['$scope', '$http', 'ngTableParams',
    function ($scope, $http, ngTableParams) {
        $http.get('/api/auction').success(function (data) {
            $scope.tableParams = new ngTableParams({
                page: 1,
                count: 10
            }, {
                total: data.length,
                getData: function ($defer, params) {
                    $defer.resolve(data.slice((params.page() - 1) * params.count(), params.page() * params.count()));
                    $scope.auctions = data
                }
            });
        });
    }]);

auctionControllers.controller('AuctionCreationController', ['$scope', '$http',
    function ($scope, $http) {
        $scope.submit = function () {
            $http.post("/api/auction", $scope.auction);
            $scope.auction = {};
        };
    }]);

auctionControllers.controller('AuctionViewController', ['$scope', '$http', '$routeParams', '$websocket',
    function ($scope, $http, $routeParams, $websocket) {

        var auctionName = $routeParams.auction;
        $scope.auction = {};
        $http.get("/api/auction/" + $routeParams.auction).success(function (data) {
            $scope.auction = data;
        });

        $scope.submit = function () {
            $http.post("/api/auction/"  + auctionName + "/bid", {name: auctionName, amount: $scope.bid.price} );
            $scope.bid = {};
        };

        var socket = new SockJS('/ws');
        stompClient = Stomp.over(socket);
        stompClient.connect({}, function(frame) {
            console.log('Connected: ' + frame);
            stompClient.send("/app/oldbids", {}, JSON.stringify({ 'name': name }));
            stompClient.subscribe('/topic/' + auctionName, function(bid) {
                console.info("Received bid " + bid);
                $scope.$apply(function() {
                    $scope.auction.bids.push(JSON.parse(bid.body));
                });
            });
        });

    }]);

auctionControllers.controller('UserCreationController', ['$scope', '$http',
    function ($scope, $http) {
        $scope.submit = function () {
            var user = $scope.user;
            $http.post("/api/user", {userName: user.userName, password: user.password, firstName: user.firstName, lastName: user.lastName, emails: [user.email]});
            $scope.user = {};
        };
    }]);
