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
                    $scope.auctions = data;
                }
            });
        });
    }]);

auctionControllers.controller('AuctionCreationController', ['$scope', '$http',
    function ($scope, $http) {
        $scope.submit = function () {
            console.info(JSON.stringify($scope.auction));
            $http.post("/api/auction", $scope.auction);
            $scope.auction = {};
        };
    }]);

auctionControllers.controller('AuctionViewController', ['$scope', '$http', '$routeParams', 'ngTableParams', '$filter',
    function ($scope, $http, $routeParams, ngTableParams, $filter) {

        var auctionName = $routeParams.auction;
        console.info("auction" + auctionName);
        $scope.auction = {};
        $scope.bids = [];
        $http.get("/api/auction/" + auctionName).success(function (data) {
            $scope.auction = data;
            $scope.tableParams = new ngTableParams({
                page: 1,
                count: 10
            }, {
                total: 0,
                getData: function ($defer, params) {
                    params.total($scope.bids.length);
                    var orderedData = $filter('orderBy')($scope.bids, "-amount");
                    var pageOfBids = orderedData.slice((params.page() - 1) * params.count(), params.page() * params.count());
                    $defer.resolve(pageOfBids);
                    $scope.filteredBids = pageOfBids;
                }
            });

        });

        $scope.submit = function () {
            $http.post("/api/auction/"  + auctionName + "/bid", {name: auctionName, amount: $scope.bid.price} );
            $scope.bid = {};
        };

        var socket = new SockJS('/ws');
        stompClient = Stomp.over(socket);
        stompClient.connect({}, function(frame) {
            console.log('Connected: ' + frame);
            stompClient.send("/api/oldbids", {}, JSON.stringify({ 'name': auctionName }));
            stompClient.subscribe('/topic/' + auctionName, function(bid) {
                $scope.bids.push(JSON.parse(bid.body));
                $scope.tableParams.reload();
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

auctionControllers.controller('AuctionViewPanelController', function() {
    this.tab = 0;
    this.selectTab = function(setTab) {
        this.tab = setTab;
    };
    this.isSelected = function(checkTab) {
        return this.tab == checkTab;
    };
});