'use strict';

angular.module('polcoApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('nominalDet', {
                parent: 'entity',
                url: '/nominalDet',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'polcoApp.nominalDet.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/nominalDet/nominalDets.html',
                        controller: 'NominalDetController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('nominalDet');
                        return $translate.refresh();
                    }]
                }
            })
            .state('nominalDetDetail', {
                parent: 'entity',
                url: '/nominalDet/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'polcoApp.nominalDet.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/nominalDet/nominalDet-detail.html',
                        controller: 'NominalDetDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('nominalDet');
                        return $translate.refresh();
                    }]
                }
            });
    });
