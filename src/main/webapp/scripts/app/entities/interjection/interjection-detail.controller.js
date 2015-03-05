'use strict';

angular.module('polcoApp')
    .controller('InterjectionDetailController', function ($scope, $stateParams, Interjection, InterjectionTrans) {
        $scope.interjection = {};
        $scope.load = function (id) {
            Interjection.get({id: id}, function(result) {
              $scope.interjection = result;
            });
        };
        $scope.load($stateParams.id);
    });
