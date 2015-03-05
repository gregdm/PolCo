'use strict';

angular.module('polcoApp')
    .controller('AdverbTransDetailController', function ($scope, $stateParams, AdverbTrans, Adverb) {
        $scope.adverbTrans = {};
        $scope.load = function (id) {
            AdverbTrans.get({id: id}, function(result) {
              $scope.adverbTrans = result;
            });
        };
        $scope.load($stateParams.id);
    });
