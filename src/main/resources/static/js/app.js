'use strict';

/* App Module */

var weatherApp = angular.module('killrAuction', [
    'ngWebsocket',
    'ngRoute',
    'ngTable',
    'auctionControllers'
])
weatherApp.config(['$routeProvider',
    function($routeProvider) {
        $routeProvider.
            when('/auction', {
                templateUrl: 'auction-list.html',
                controller: 'AuctionListController'
            }).
            when('/auction-create', {
                templateUrl: 'auction-create.html',
                controller: 'AuctionCreationController'
            }).
            otherwise({
                redirectTo: '/auction'
            });
    }]);