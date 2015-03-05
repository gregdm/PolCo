'use strict';

angular.module('polcoApp')
    .controller('InterjectionTransDetailController', function ($scope, $stateParams, InterjectionTrans, Interjection) {
        $scope.interjectionTrans = {};
        $scope.load = function (id) {
            InterjectionTrans.get({id: id}, function(result) {
              $scope.interjectionTrans = result;
            });
        };
        $scope.load($stateParams.id);
    });
